import { Box, Paper, styled } from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import { useDeleteImageMutation } from "../../api/attachmentSlice";
import { useParams } from "react-router-dom";


const Label = styled(Paper)(({ theme }) => ({
    ...theme.typography.body2,
    backgroundColor: theme.palette.secondary,
    padding: theme.spacing(0.5),
    textAlign: 'center',
    color: theme.palette.text.secondary,
    borderTopLeftRadius: 0,
    borderTopRightRadius: 0,
}));

const ImageCard = ({ imageURL, imageName }) => {

    const { directory } = useParams();
    const [ deleteImage ] = useDeleteImageMutation();

    const onImageDelete = async () => {
        await deleteImage({directory, imageName}).unwrap();
    };

    return (
        <Box>
            <img
                width={30}
                src={imageURL}
                loading="lazy"
                style={{
                    borderRadius: 4,
                    display: 'block',
                    width: '100%'
                }}
            />
            <Label>
                <Box
                    sx={{
                        display: "flex",
                        p: 1
                    }}
                >
                    <DeleteIcon 
                        onClick={onImageDelete}
                        sx={{ cursor: "pointer" }} 
                    />
                </Box>
            </Label>
        </Box>
    );
};

export default ImageCard;