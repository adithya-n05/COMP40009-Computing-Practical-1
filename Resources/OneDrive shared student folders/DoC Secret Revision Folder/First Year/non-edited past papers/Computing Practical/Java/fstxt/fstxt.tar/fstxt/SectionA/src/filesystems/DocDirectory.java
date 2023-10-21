package filesystems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DocDirectory extends DocFile {

  List<DocFile> docFileList;

  public DocDirectory(String name) {
    super(name);
    docFileList = new ArrayList<>();
  }

  @Override
  public int getSize() {
    return getName().length();
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
    throw new UnsupportedOperationException("Cannot convert directory to data file");
  }

  @Override
  public DocFile duplicate() {
    DocDirectory newDocDirectory = new DocDirectory(getName());
    for (DocFile docFile : docFileList) {
      newDocDirectory.addFile(docFile.duplicate());
    }
    return newDocDirectory;
  }

  public void addFile(DocFile file) {
    if (containsFile(file.getName())) {
      throw new IllegalArgumentException();
    }
    docFileList.add(file);
  }

  public boolean removeFile(String file) {
    for (int i = 0; i < docFileList.size(); i++) {
      if (docFileList.get(i).getName().equals(file)) {
        docFileList.remove(i);
        return true;
      }
    }
    return false;
  }

  public Set<DocFile> getAllFiles() {
    return new HashSet<>(docFileList);
  }

  public boolean containsFile(String file) {
    for (DocFile docFile : docFileList) {
      if (docFile.getName().equals(file)) {
        return true;
      }
    }
    return false;
  }

  public Set<DocDirectory> getDirectories() {
    return docFileList
        .stream()
        .filter(x -> x instanceof DocDirectory)
        .map(x -> (DocDirectory) x)
        .collect(Collectors.toSet());
  }

  public Set<DocDataFile> getDataFiles() {
    return docFileList
        .stream()
        .filter(x -> x instanceof DocDataFile)
        .map(x -> (DocDataFile) x)
        .collect(Collectors.toSet());
  }

  public DocFile getFile(String filename) {
    for (DocFile docFile : docFileList) {
      if (docFile.getName().equals(filename)) {
        return docFile;
      }
    }
    throw new RuntimeException("file not in directory.");
    // This is because we assume the item to be contained in the directory.
  }

}
