package com.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Box;
import com.models.Cita;
import com.models.User;
import com.repository.BoxRepository;
import com.repository.CitaRepository;
import com.repository.UserRepository;
import com.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/usuario") 
public class UsuarioController {

    private final UserRepository userRepository;
    private final BoxRepository boxRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder encoder;

    public UsuarioController(UserRepository userRepository, BoxRepository boxRepository, CitaRepository citaRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.boxRepository = boxRepository;
        this.citaRepository = citaRepository;
        this.encoder = encoder;
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listarUsuarios() {
        List<User> usuarios = userRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ADMIN')")
    public ResponseEntity<User> getUsuario(@PathVariable Long userId) {
        Optional<User> usuarioOptional = userRepository.findById(userId);

        return usuarioOptional.map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/instrumento/{instrumento}")
    public ResponseEntity<List<User>> getUsuariosInstrumento(@PathVariable String instrumento) {
        List<User> usuarios = userRepository.findByInstrumento(instrumento);
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @DeleteMapping("eliminar/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long userId) {
        return userRepository.findById(userId)
                             .map(user -> {
                                 userRepository.delete(user);
                                 return ResponseEntity.ok().build();
                             })
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ADMIN')")
    public ResponseEntity<User> actualizarUsuario(@PathVariable Long userId, @RequestBody User usuarioActualizado) {
        return userRepository.findById(userId)
                             .map(usuario -> {
                                 if (!usuarioActualizado.getUsername().isEmpty()) usuario.setUsername(usuarioActualizado.getUsername());
                                 if (!usuarioActualizado.getEmail().isEmpty()) usuario.setEmail(usuarioActualizado.getEmail());
                                 if (!usuarioActualizado.getPassword().isEmpty()) usuario.setPassword(encoder.encode(usuarioActualizado.getPassword()));
                                 userRepository.save(usuario);
                                 return ResponseEntity.ok(usuario);
                             })
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/datos/{userId}")
    @PreAuthorize("hasRole('MEDICO') or hasRole('ADMIN')")
    public ResponseEntity<User> actualizarDatosPersonales(@PathVariable Long userId, @RequestBody User usuarioActualizado) {
        return userRepository.findById(userId)
                             .map(usuario -> {
                                 usuario.setRut(usuarioActualizado.getRut());
                                 usuario.setNombre(usuarioActualizado.getNombre());
                                 usuario.setApellido(usuarioActualizado.getApellido());
                                 userRepository.save(usuario);
                                 return ResponseEntity.ok(usuario);
                             })
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/actualizarBox/{boxId}")
    public ResponseEntity<String> actualizarBoxUsuarioLogueado(@PathVariable Long boxId, Authentication authentication) {
        Long usuarioId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        Optional<User> userOptional = userRepository.findById(usuarioId);
        Optional<Box> boxOptional = boxRepository.findById(boxId);

        if (userOptional.isPresent() && boxOptional.isPresent()) {
            User user = userOptional.get();
            Box box = boxOptional.get();
            user.setBox(box);
            userRepository.save(user);
            box.setEstado(false);
            boxRepository.save(box);
            return ResponseEntity.ok("Box actualizado exitosamente");
        } else {
            return ResponseEntity.status(404).body("Box no encontrado");
        }
    }

    @PutMapping("/liberarBox/{boxId}")
    public ResponseEntity<String> liberarBoxUsuarioLogueado(@PathVariable Long boxId, Authentication authentication) {
        Long usuarioId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        Optional<User> userOptional = userRepository.findById(usuarioId);
        Optional<Box> boxOptional = boxRepository.findById(boxId);

        if (userOptional.isPresent() && boxOptional.isPresent()) {
            User user = userOptional.get();
            Box box = boxOptional.get();
            user.setBox(null);
            userRepository.save(user);
            box.setEstado(true);
            boxRepository.save(box);
            return ResponseEntity.ok("Box actualizado exitosamente");
        } else {
            return ResponseEntity.status(404).body("Box no encontrado");
        }
    }

    @PutMapping("/transferirCitas/{usuarioOrigenId}/{usuarioDestinoId}")
    public ResponseEntity<?> transferirCitas(@PathVariable Long usuarioOrigenId, @PathVariable Long usuarioDestinoId) {
        User usuarioOrigen = userRepository.findById(usuarioOrigenId)
                                           .orElseThrow(() -> new RuntimeException("Usuario origen no encontrado"));
        User usuarioDestino = userRepository.findById(usuarioDestinoId)
                                            .orElseThrow(() -> new RuntimeException("Usuario destino no encontrado"));

        List<Cita> citasUsuarioOrigen = citaRepository.findByProfesionalId(usuarioOrigenId);
        citasUsuarioOrigen.forEach(cita -> cita.setProfesional(usuarioDestino));
        citaRepository.saveAll(citasUsuarioOrigen);

        return ResponseEntity.ok(Map.of("mensaje", "Citas transferidas exitosamente", "cantidadCitas", citasUsuarioOrigen.size()));
    }
}
