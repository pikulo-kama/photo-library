import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useUserInfo } from "../hooks/useUserInfo";


const RequireAuth = () => {

    const location = useLocation();
    const { userInfo, isLoading } = useUserInfo();

    if (isLoading) {
        return;
    }

    return (
        userInfo ? <Outlet /> : <Navigate to="/" state={{from: location}} replace />
    );
};

export default RequireAuth;