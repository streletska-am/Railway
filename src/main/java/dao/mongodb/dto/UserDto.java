package dao.mongodb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.entity.User;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class UserDto extends User {

    private ObjectId objectId;

    public UserDto(Long id, String name, String surname, String phone, String email, String password, Boolean admin, ObjectId objectId) {
        super(id, name, surname, phone, email, password, admin);
        this.objectId = objectId;
    }

    public UserDto(User user) {
        super(user.getId(), user.getName(), user.getSurname(), user.getPhone(), user.getEmail(), user.getPassword(), user.getAdmin());

        if (UserDto.class.equals(user.getClass())) {
            this.objectId = ((UserDto) user).getObjectId();
        }
    }
}
