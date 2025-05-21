package br.com.imersao_app.demo.controller;

import br.com.imersao_app.demo.dto.AuthRequest;
import br.com.imersao_app.demo.dto.AuthResponse;
import br.com.imersao_app.demo.dto.JwtResponse;
import br.com.imersao_app.demo.dto.LoginRequest;
import br.com.imersao_app.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "API para autenticação e registro de usuários")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	@Operation(summary = "Autenticar usuário", description = "Realiza login e retorna token JWT")
	public ResponseEntity<ResponseEntity<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.autenticar(request));
	}

	@PostMapping(path = "/register", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	@Operation(summary = "Registrar novo usuário", description = "Cria uma nova conta de usuário")
	public ResponseEntity<AuthResponse> cadastrar(@Valid @RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}
}