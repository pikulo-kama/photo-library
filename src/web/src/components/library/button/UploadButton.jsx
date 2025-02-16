import { Button } from "@mui/material";
import { FaCloudUploadAlt } from "react-icons/fa";
import VisuallyHiddenInput from "../../VisuallyHiddenInput";


const UploadButton = ({ title, onUpload }) => {

    const onFileUpload = async (event) => {
        const file = event.target.files[0];
        onUpload(file);
    };

    return (
        <Button
            component="label"
            tabIndex={-1}
            role={undefined}
            size="small"
            color="secondary"
            variant="contained"
            startIcon={<FaCloudUploadAlt />}
        >
            {title}
            <VisuallyHiddenInput
                accept="image/*"
                type="file"
                onChange={onFileUpload}
            />
        </Button>
    );
};

export default UploadButton;
