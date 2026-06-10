package br.edu.utfpr.pb.pw44s.server.service;

import br.edu.utfpr.pb.pw44s.server.dto.UserDTO;
import br.edu.utfpr.pb.pw44s.server.model.Authority;
import br.edu.utfpr.pb.pw44s.server.model.User;
import br.edu.utfpr.pb.pw44s.server.repository.AuthorityRepository;
import br.edu.utfpr.pb.pw44s.server.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository,
                       ModelMapper modelMapper

    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.modelMapper = modelMapper;

    }
    
    public void save(User user) {
        user.setPassword( passwordEncoder.encode(user.getPassword()) );

        Set<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByAuthority("ROLE_USER"));
        user.setUserAuthorities(authorities);
        user.setActive(true);

        this.userRepository.save(user);
    }

    public ResponseEntity<List<UserDTO>> findAllUsersClients() {
        List<User> users = userRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        List<UserDTO> returnedUserss = users.stream()
                .filter(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")))
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(returnedUserss);
    }

    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<User> users = userRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();

        List<UserDTO> returnedUsers = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(returnedUsers);
    }

    public UserDTO updateUserStatus(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setActive(userDTO.isActive());
        User userSalvo = userRepository.save(user);

        // Retorna o DTO atualizado do banco
        return modelMapper.map(userSalvo, UserDTO.class);
    }

    public UserDTO updateUserPermission(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // CORREÇÃO DO CAST: Converte de forma segura a coleção do DTO para Set de Authority
        if (userDTO.getUserAuthorities() != null) {
            Set<Authority> authorities = userDTO.getUserAuthorities().stream()
                    .map(authDto -> modelMapper.map(authDto, Authority.class))
                    .collect(Collectors.toSet());
            user.setUserAuthorities(authorities);
        }

        User userSalvo = userRepository.save(user);

        return modelMapper.map(userSalvo, UserDTO.class);
    }
}
