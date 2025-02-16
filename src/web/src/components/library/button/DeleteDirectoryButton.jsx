import { Button } from "@mui/material";
import { Delete } from "@mui/icons-material";


const DeleteDirectoryButton = ({ title, onDelete }) => {

    return (
        <Button
            size="small"
            color="primary"
            variant="contained"
            startIcon={<Delete />}
            onClick={onDelete}
        >
            {title}
        </Button>
    );
};

export default DeleteDirectoryButton;
