package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.login.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.login.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}
