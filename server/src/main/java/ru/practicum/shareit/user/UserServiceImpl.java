package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserCreateDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistException(userDto.getEmail());
        }
        User user = userRepository.save(userMapper.toModel(userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userDto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserAlreadyExistException(userDto.getEmail());
            }
            user.setEmail(userDto.getEmail());
        }
        User userSaved = userRepository.save(user);
        return userMapper.toDto(userSaved);
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(userId));
        return userMapper.toDto(user);
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException(id));
        List<Item> itemsByUser = itemRepository.findByOwner(user);
        if (!itemsByUser.isEmpty()) {
            itemRepository.deleteAll(itemsByUser);
        }
        userRepository.deleteById(id);
    }
}

