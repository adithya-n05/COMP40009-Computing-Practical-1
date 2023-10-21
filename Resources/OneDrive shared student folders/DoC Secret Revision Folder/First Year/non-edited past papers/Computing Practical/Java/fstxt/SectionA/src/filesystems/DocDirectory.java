package filesystems;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DocDirectory extends DocFile {
  private final Set<DocFile> files;

  public DocDirectory(String name) {
    super(name);
    files = new HashSet<>();
  }

  @Override
  public int getSize() {
    return super.getName().length();
  }

  @Override
  public boolean isDirectory() {
    return true;
  }

  @Override
  public boolean isDataFile() {
    return false;
  }

  @Override
  public DocDirectory asDirectory() {
    return this;
  }

  @Override
  public DocDataFile asDataFile() {
    throw new UnsupportedOperationException("Cannot represent a directory as a data file!");
  }

  @Override
  public DocFile duplicate() {
    DocDirectory result = new DocDirectory(getName());
    result.files.addAll(getAllFiles().stream().map(DocFile::duplicate).collect(Collectors.toSet()));
    return result;
  }

  public boolean containsFile(String name) {
    return files.stream().anyMatch((file) -> name.equals(file.getName()));
  }

  public Set<DocFile> getAllFiles() {
    return files;
  }

  public Set<DocDirectory> getDirectories() {
    return files.stream()
        .filter(DocFile::isDirectory)
        .map(DocFile::asDirectory)
        .collect(Collectors.toSet());
  }

  public Set<DocDataFile> getDataFiles() {
    return files.stream()
        .filter(DocFile::isDataFile)
        .map(DocFile::asDataFile)
        .collect(Collectors.toSet());
  }

  public void addFile(DocFile file) {
    if (containsFile(file.getName())) {
      throw new IllegalArgumentException(
          "Directory already contains a fail with name: " + file.getName());
    }

    files.add(file);
  }

  public boolean removeFile(String filename) {
    if (!containsFile(filename)) {
      return false;
    }

    files.remove(getFile(filename));
    return true;
  }

  public DocFile getFile(String filename) {
    return files.stream().filter((file) -> filename.equals(file.getName())).findFirst().get();
  }
}
