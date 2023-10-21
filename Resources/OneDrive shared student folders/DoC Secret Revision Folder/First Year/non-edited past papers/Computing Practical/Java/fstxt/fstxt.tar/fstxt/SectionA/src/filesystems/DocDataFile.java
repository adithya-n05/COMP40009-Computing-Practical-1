package filesystems;

import java.util.ArrayList;
import java.util.Arrays;

public class DocDataFile extends DocFile {
  byte[] bytes;

  /**
   * Construct a file with the given name.
   *
   * @param name The name of the file.
   */
  DocDataFile(String name, byte[] bytes) {
    super(name);
    this.bytes = bytes;
  }

  /**
   * computes the size of the file.
   *
   * @return Returns the size of the name + the size of the array.
   */
  @Override
  public int getSize() {
    return getName().length() + bytes.length;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  /**
   * Compares the two objects to see if they are equal.
   *
   * @param oth the object being compared to.
   * @return returns whether they are equal.
   */
  @Override
  public boolean equals(Object oth) {
    if (oth instanceof DocDataFile other) {
      return this.hashCode() == other.hashCode()
          && oth.getClass() == this.getClass(); // To prevent subclasses from returning true.
    }
    return false;
  }

  @Override
  public int hashCode() { // Same names and same sized contents will be considered the same
    return getName().hashCode() + Arrays.hashCode(bytes);
  }

  @Override
  public boolean isDataFile() {
    return true;
  }

  @Override
  public DocDirectory asDirectory() {
    throw new UnsupportedOperationException("Cannot convert return a directory for a file.");
  }

  @Override
  public DocDataFile asDataFile() {
    return this;
  }

  @Override
  public DocFile duplicate() {
    return new DocDataFile(getName(), bytes);
  }

  public boolean containsByte(byte checkByte) {
    for (byte byteInArray : bytes) {
      if (checkByte == byteInArray) {
        return true;
      }
    }
    return false;
  }

}
