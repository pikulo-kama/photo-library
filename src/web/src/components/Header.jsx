import { AppBar, Avatar, Toolbar, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useUserInfo } from "../hooks/useUserInfo";
import { AiOutlinePicture } from "react-icons/ai";


const Header = () => {

    const navigate = useNavigate();
    const { userInfo } = useUserInfo();

    return (
        <>
            <AppBar position="fixed" color="primary">
                <Toolbar sx={{ gap: 1 }}>
                    <AiOutlinePicture size={30} />
                    <Typography 
                        variant="h6"
                        onClick={() => navigate("/")}
                        sx={{ flexGrow: 1, cursor: "pointer" }}
                    >
                        KAMA's Photo Library   
                    </Typography>
                    {
                        !userInfo ? null :
                            <Avatar 
                                alt={userInfo.name} 
                                src={userInfo.picture}
                            />
                    }
                </Toolbar>
            </AppBar>
    </>
    )
}

export default Header;
