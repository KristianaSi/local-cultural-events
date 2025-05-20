package com.kristiana.application.contract;
import com.kristiana.application.dto.AddDtoUser;
import java.util.function.Supplier;
public interface SignUpService {
    void signUp(AddDtoUser addDtoUser, Supplier<String> waitForUserInput);
}
