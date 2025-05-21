const api = axios.create({
    baseURL: '/api',
    timeout: 10000,
});

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; // Deve começar com "Bearer "
    }
    return config;
}, error => {
    return Promise.reject(error);
});

api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login?error=session_expired';
        }
        return Promise.reject(error);
    }
);