package filesystems;

import java.util.Optional;

public class DocFileUtils {

  /**
   * Compute the total size, in bytes, of the directory and all of its contents.
   *
   * @param directory A directory.
   * @return The size of this directory plus, the sum of the sizes of all files contained directly
   *     or indirectly in this directory.
   */
  public static int getTotalDirectorySize(DocDirectory directory) {
    int subDirectorySize =
        directory.getDirectories().stream()
            .map(DocFileUtils::getTotalDirectorySize)
            .mapToInt(Integer::intValue)
            .sum();
    int dataFileSize =
        directory.getDataFiles().stream().map(DocFile::getSize).mapToInt(Integer::intValue).sum();
    return subDirectorySize + dataFileSize + directory.getSize();
  }

  /**
   * Copy a named file between directories.
   *
   * @param src A source directory.
   * @param dst A destination directory.
   * @param filename The name of a file to be copied.
   * @return False if the source directory does not contain a file with the given name, or if the
   *     destination directory already contains a file with the given name. Otherwise, create a
   *     duplicate of the file with the given name in the source directory and add this duplicate to
   *     the destination directory.
   */
  public static boolean copy(DocDirectory src, DocDirectory dst, String filename) {
    if (!src.containsFile(filename) || dst.containsFile(filename)) {
      return false;
    }

    dst.addFile(src.getFile(filename).duplicate());
    return true;
  }

  /**
   * Locate a file containing a given byte and lying at or beneath a given file, if one exists.
   *
   * @param root A file, to be recursively searched.
   * @param someByte A byte to be searched for.
   * @return An empty optional if no file at or beneath the given root file contains the given byte.
   *     Otherwise, return an optional containing any such file.
   */
  public static Optional<DocDataFile> searchForByte(DocFile root, byte someByte) {
    if (root.isDataFile()) {
      if (root.asDataFile().containsByte(someByte)) {
        return Optional.of(root.asDataFile());
      } else {
        return Optional.empty();
      }
    }

    // root.isDirectory()
    return root.asDirectory().getAllFiles().stream()
        .map((dir) -> searchForByte(dir, someByte))
        .reduce(DocFileUtils::optReduce)
        .orElse(Optional.empty());
  }

  private static Optional<DocDataFile> optReduce(
      Optional<DocDataFile> first, Optional<DocDataFile> second) {
    if (first.isEmpty() && second.isEmpty()) {
      return Optional.empty();
    } else if (first.isPresent()) {
      return first;
    } else {
      return second;
    }
  }
}
