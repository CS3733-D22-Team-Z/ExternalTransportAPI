package edu.wpi.cs3733.D22.teamZ.api.entity;

public enum TransportMethod {
  HELICOPTER("HELICOPTER"),
  AMBULANCE("AMBULANCE"),
  PATIENTCAR("PATIENTCAR"),
  PLANE("PLANE");

  private final String methodStr;

  TransportMethod(String methodStr) {
    this.methodStr = methodStr;
  }

  /**
   * Converts this TransportMethod into a String
   *
   * @return A String representing this TransportMethod
   */
  public String toString() {
    return this.methodStr;
  }

  /**
   * Returns a TransportMethod based on the String provided
   *
   * @param methodStr The String used to base the TransportMethod on
   * @return The TransportMethod associated with the String provided or null if no TransportMethod
   *     is found
   */
  public static TransportMethod fromString(String methodStr) {
    for (TransportMethod method : TransportMethod.values()) {
      if (method.toString().equalsIgnoreCase(methodStr)) {
        return method;
      }
    }
    return null;
  }
}
