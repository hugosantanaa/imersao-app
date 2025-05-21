document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(loginForm);
            const payload = Object.fromEntries(formData);
            
            try {
                const response = await api.post('/api/auth/login', payload);
                localStorage.setItem('token', response.data.token);
                window.location.href = '/dashboard';
            } catch (error) {
                showError('Credenciais inválidas');
            }
        });
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(registerForm);
            const payload = Object.fromEntries(formData);
            
            try {
                await api.post('/api/auth/register', payload);
                showSuccess('Registro realizado com sucesso! Faça login.');
                setTimeout(() => window.location.href = '/login', 2000);
            } catch (error) {
                showError(error.response?.data?.message || 'Erro no registro');
            }
        });
    }
});

function showError(_message) {
    // Implementação similar ao showToast do router
}

function showSuccess(_message) {
    // Implementação similar ao showToast do router
}