package com.backend.sistemarestaurante.modules.usuarios;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
    *  Metodo que busca los usuarios y los traiga desde la base de datos
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscar el usuario por el correo
        Usuario usuario = usuarioRepository.findUsuarioByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        // Agregar los roles a la lista de autorizacion
        usuario.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        // Agregar los permisos
        usuario.getRoles().stream()
                .flatMap(role -> role.getPermissionSet().stream())
                .forEach(pemission -> authorityList.add(new SimpleGrantedAuthority(pemission.getNombre())));

        // Retornar el usuario encontrado en lenguaje de sprint security
        return new User(usuario.getEmail(), usuario.getPassword(), usuario.isEnable(), usuario.isAccountNonExpired(),
                usuario.isCredentialsNonExpired(), usuario.isAccountNonLocked(), authorityList);
    }
}
