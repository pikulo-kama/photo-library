import { useGetUserInfoQuery } from "../api/authSlice";


export const useUserInfo = () => {

    const { data: userInfo, isLoading } = useGetUserInfoQuery();

    return {
        userInfo,
        isLoading
    };
};
