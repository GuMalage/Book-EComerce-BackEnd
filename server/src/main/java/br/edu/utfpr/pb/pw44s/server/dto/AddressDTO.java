package br.edu.utfpr.pb.pw44s.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private String logradouro;
    private String complement;
    private String cep;
    private UserDTO user;
}
