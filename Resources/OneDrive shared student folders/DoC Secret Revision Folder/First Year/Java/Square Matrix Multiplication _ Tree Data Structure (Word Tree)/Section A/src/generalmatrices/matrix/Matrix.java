package generalmatrices.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

public class Matrix<T> {

  private final int order;
  private T[][] contents;

  public Matrix(List<T> elements) {
    if (elements.isEmpty()) {
      throw new IllegalArgumentException("A matrix cannot be empty");
    }
    order = (int) Math.sqrt(elements.size());
    contents = (T[][]) new Object[order][order];
    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        contents[i][j] = elements.get(i * order + j);
      }
    }
  }

  public T get(int row, int col) {
    return contents[row][col];
  }

  public int getOrder() {
    return order;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder("[");
    for (int i = 0; i < order; i++) {
      string.append("[");
      for (int j = 0; j < order - 1; j++) {
        string.append(contents[i][j] + " ");
      }
      string.append(contents[i][order - 1] + "]");
    }
    string.append("]");
    return string.toString();
  }

  public Matrix<T> sum(Matrix<T> other, BinaryOperator<T> elementSum) {
    if (this.order != other.order) {
      throw new RuntimeException("Matrices have different orders, cannot add");
    }
    List<T> results = new ArrayList<>();
    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        results.add(elementSum.apply(contents[i][j], other.contents[i][j]));
      }
    }

    return new Matrix<>(results);
  }

  public Matrix<T> product(Matrix<T> other, BinaryOperator<T> elementSum,
      BinaryOperator<T> elementProduct) {
    if (this.order != other.order) {
      throw new RuntimeException(
          "Matrices have different orders, cannot multiply");
    }
    List<T> results = new ArrayList<>();

    for (int i = 0; i < order; i++) {
      for (int j = 0; j < order; j++) {
        T result = elementProduct.apply(contents[i][0], other.contents[0][j]);
        for (int k = 1; k < order; k++) {
          result = elementSum.apply(result,
              elementProduct.apply(contents[i][k], other.contents[k][j]));
        }
        results.add(result);
      }
    }

    return new Matrix<>(results);
  }


}
