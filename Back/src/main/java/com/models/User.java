package com.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;
  //Por temas practicos por ahora username sera Nombre

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @Column(name = "rut")
  private String rut;

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "direccion")
  private String direccion;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", 
             joinColumns = @JoinColumn(name = "user_id"),
             inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne(cascade = CascadeType.ALL) 
  @JoinColumn(name = "box", referencedColumnName = "id") 
  private Box box;

  @OneToMany(mappedBy = "profesional")
  private Set<AsistenciaMedica> asistenciasMedicas;

  @OneToMany(mappedBy = "profesional")
  private Set<Cita> citas;

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public String getRut(){
    return rut;
  }

  public void setRut(String rut){
    this.rut = rut;
  }

  public String getNombre(){
    return nombre;
  }

  public void setNombre(String nombre){
    this.nombre = nombre;
  }

  public String getApellido(){
    return apellido;
  }

  public void setApellido(String apellido){
    this.apellido = apellido;
  }

  public String getDireccion(){
    return direccion;
  }

  public void setDireccion(String direccion){
    this.direccion = direccion;
  }

  public Box getBox(){
    return box;
  }

  public void setBox(Box box){
    this.box = box;
  }

  public Set<AsistenciaMedica> getAistAsistenciaMedicas(){
    return asistenciasMedicas;
  }

  public void setAsistenciasMedicas(Set<AsistenciaMedica> asistenciasMedicas){
    this.asistenciasMedicas = asistenciasMedicas;
  }

public Object getEspecialidad() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getEspecialidad'");
}

}
