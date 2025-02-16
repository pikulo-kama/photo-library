import { List, ListItemButton, ListItemIcon, ListItemText, Typography } from "@mui/material";
import { FaFolder } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

const DirectoryBar = ({ directories }) => {

    const navigate = useNavigate();

    return (
        <List>
            {
                directories.map((directory, index) =>
                    <ListItemButton
                        key={index}
                        onClick={() => navigate(`/library/${directory.directoryUUID}`)}
                    >
                        <ListItemIcon>
                            <FaFolder size={40} />
                        </ListItemIcon>
                        <ListItemText primary={directory.directoryName} />
                    </ListItemButton>
                )
            }
        </List>
    )
};

export default DirectoryBar;
