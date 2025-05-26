package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.exception.UserAlreadyExistException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserCreateDto userDto) {
        if (userRepository.isEmailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException(userDto.getEmail());
        }
        User user = userRepository.createUser(userMapper.toModel(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userDto, long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (userRepository.isEmailExist(userDto.getEmail())) {
                throw new UserAlreadyExistException(userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }
        User userSaved = userRepository.updateUser(user);
        return userMapper.toDto(userSaved);
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundUserException(id));
        userRepository.deleteUser(id);
        List<Item> itemsByUser = itemRepository.getItemsByUser(id);
        for (Item item : itemsByUser) {
            itemRepository.deleteItem(item.getId());
        }
    }
}

