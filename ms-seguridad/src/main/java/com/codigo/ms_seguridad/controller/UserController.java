package com.codigo.ms_seguridad.controller;

import com.codigo.ms_seguridad.aggregates.request.UsuarioRequest;
import com.codigo.ms_seguridad.aggregates.response.UsuarioResponse;
import com.codigo.ms_seguridad.entity.Usuario;
import com.codigo.ms_seguridad.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;
    @GetMapping("/user")
    public ResponseEntity<List<Usuario>> obtenerUser(){

        return ResponseEntity.ok(usuarioService.obtenertodos("USER"));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<Usuario>> obtenerAdmin(){

        return ResponseEntity.ok(usuarioService.obtenertodos("ADMIN"));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Usuario> actualizarUser(
            @PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioRequest);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<Usuario> actualizarAdmin(
            @PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioRequest);
        return ResponseEntity.ok(usuarioActualizado);
    }
}
