import Cookies from "universal-cookie";

export const cookie = new Cookies(null, { path: "/" });

export const getToken = () => cookie.get("token");

export const setToken = (token: string) => cookie.set("token", token);

export const removeToken = () => cookie.remove("token");
