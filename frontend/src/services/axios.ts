import { toast } from "react-toastify";
import { createBrowserHistory } from "history";
import axios from "axios";

const instance = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,
  timeout: 30000,
  headers: {
    "Content-type": "application/json",
    "Access-Control-Allow-Credentials": true,
  },
});

/* Apply Interceptor */
// HTTP request interceptor
instance.interceptors.request.use(
  (config) => {
    const user = localStorage.getItem("token");
    if (user) {
      config.headers!.Authorization = "Bearer " + user;
    }
    return config;
  },
  (err) => {
    return Promise.reject(err);
  }
);

// HTTP response interceptor
instance.interceptors.response.use(
  (response) => {
    return response;
  },
  function (error) {
    if (error.response) {
      const history = createBrowserHistory();
      console.log(error.response);
      switch (error.response.status) {
        /* 'JWT expired' exeption */
        case 400:
          console.log("400 ERROR, not authorized.");
          break;
        case 401:
          toast("다시 로그인 해주세요.", {
            icon: "🌼",
            style: {
              borderRadius: "10px",
              background: "#333",
              color: "#fff",
            },
          });
          history.push("/login");
          // 강제로 새로고침 (임시)
          window.location.reload();
          //  2. Reset authentication from localstorage/sessionstorage
          localStorage.removeItem("token");
          // logout();
          break;
        case 404:
          console.log("404error!");
          break;
        case 409:
          console.log("409error!");
          break;
        default:
      }
    } else {
      // ex. 서버 키지 않은 경우
    }
    return Promise.reject(error);
    // return false;
  }
);

export const multipartInstance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  timeout: 30000,
  headers: {
    "Content-Type": `multipart/form-data`,
  },
});

export default instance;
