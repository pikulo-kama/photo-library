import { Masonry } from "@mui/lab";
import { useGetImagesQuery } from "../../api/attachmentSlice";
import { useParams } from "react-router-dom";
import ImageCard from "./ImageCard";


const ImageGrid = () => {

    const { directory } = useParams();
    const { data, isLoading } = useGetImagesQuery(directory);

    if (isLoading) {
        return;
    }

    let i = 0;
    let images = [];

    while (i < 10) {
        images = images.concat(data.response);
        i++;
    }

    return (
        <Masonry columns={{ xs: 1, md: 3, xl: 5}} spacing={2}>
            {
                images.map((image, index) =>
                    <ImageCard 
                        key={index}
                        imageURL={image.imageURL}
                        imageName={image.imageName}
                    />
                )
            }
        </Masonry>
    );
};

export default ImageGrid;