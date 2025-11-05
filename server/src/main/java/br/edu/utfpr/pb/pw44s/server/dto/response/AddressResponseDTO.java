package br.edu.utfpr.pb.pw44s.server.dto.response;

import br.edu.utfpr.pb.pw44s.server.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private String street;
    private String complement;
    private UserDTO user;
    private String city;
    private String state;
    private String houseNumber;
}
