package com.adrabazha.photo_library.util;

import java.util.Arrays;
import java.util.StringJoiner;

public class CloudUtil {

    private static final String GCS_PATH_SEPARATOR = "/";
    private static final String TRIM_LEADING_TRAILING_SEPARATOR_REGEX = "^" + GCS_PATH_SEPARATOR + "|" + GCS_PATH_SEPARATOR + "$";

    public static final String DIRECTORY_PREFIX = "DIR-";
    public static final String IMAGE_PREFIX = "IMG-";

    /**
     * Used to build GCS compliant file string, so it would be interpreted
     * as a directory. Will use '/' as delimiter.
     *
     * @param parts path parts that should be joined
     * @return path to file in GCS
     */
    public static String buildCloudPath(String... parts) {

        StringJoiner joiner = new StringJoiner(GCS_PATH_SEPARATOR);

        Arrays.stream(parts)
                .filter(part -> part != null && !part.isBlank())
                .map(part -> part.replaceAll(TRIM_LEADING_TRAILING_SEPARATOR_REGEX, ""))
                .forEach(joiner::add);

        return joiner.toString();
    }

    /**
     * Used to get parent directory of an image.
     * Splits image name by separator and returns first parent directory
     * of image.
     *
     * @param imageName image for which parent directory should be obtained
     * @return name of image parent directory
     */
    public static String getParentDirectoryFromPath(String imageName) {
        return getSegmentFromImage(imageName, DIRECTORY_PREFIX);
    }

    /**
     * Used to get image name based on provided image full name (including directories).
     *
     * @param imageName full image name
     * @return image name (excluding directories)
     */
    public static String getImageNameFromPath(String imageName) {
        return getSegmentFromImage(imageName, IMAGE_PREFIX);
    }

    private static String getSegmentFromImage(String imageName, String segmentIdentifier) {

        String[] pathSegments = imageName.split(GCS_PATH_SEPARATOR);

        for (int segmentId = pathSegments.length - 1; segmentId >= 0; segmentId--) {

            String pathSegment = pathSegments[segmentId];

            if (pathSegment.startsWith(segmentIdentifier)) {
                return pathSegment;
            }
        }

        return null;
    }
}
