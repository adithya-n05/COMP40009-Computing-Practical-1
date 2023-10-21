package generalmatrices.examples;

import generalmatrices.matrix.Matrix;
import generalmatrices.pair.PairWithOperators;
import java.util.List;

public class Example {

  public static Matrix<PairWithOperators> multiplyPairMatrices(
      List<Matrix<PairWithOperators>> matrices) {

    if (matrices.isEmpty()) {
      throw new RuntimeException("Empty matrices list");
    }

    if (matrices.size() == 1) {
      return matrices.get(0);
    }

    return matrices.stream().reduce((m1, m2) -> m1
        .product(m2, PairWithOperators::sum, PairWithOperators::product)).get();
  }

}
