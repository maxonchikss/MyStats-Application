package com.example.mystats.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mystats.data.repository.WorkoutRepository;
import com.example.mystats.model.User;

public class LoginViewModel extends AndroidViewModel {
    private final WorkoutRepository repository;
    private final MutableLiveData<User> userLiveData;
    private final MutableLiveData<Boolean> loginSuccess;
    private final MutableLiveData<String> errorMessage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        userLiveData = new MutableLiveData<>();
        loginSuccess = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void login(String email, String password) {
        new Thread(() -> {
            User user = repository.login(email, password);
            if (user != null) {
                userLiveData.postValue(user);
                loginSuccess.postValue(true);
            } else {
                loginSuccess.postValue(false);
                errorMessage.postValue("Неверный email или пароль");
            }
        }).start();
    }

    public long registerUser(String name, String email, String password) {
        User existingUser = repository.getUserByEmail(email);
        if (existingUser != null) {
            return -1;
        }
        User newUser = new User(name, email, password);
        return repository.registerUser(newUser);
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
}