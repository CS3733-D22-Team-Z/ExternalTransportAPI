package edu.wpi.cs3733.D22.teamZ.entity;

public enum RequestStatus {
  UNASSIGNED("UNASSIGNED"),
  PROCESSING("PROCESSING"),
  DONE("DONE");

  private final String statusStr;

  RequestStatus(String statusStr) {
    this.statusStr = statusStr;
  }

  /**
   * Converts this RequestStatus into a String
   *
   * @return A String representing this RequestStatus
   */
  public String toString() {
    return this.statusStr;
  }

  /**
   * Returns a RequestStatus based on the String provided
   *
   * @param statusStr The String used to base the RequestStatus on
   * @return The RequestStatus associated with the String provided or null if no RequestStatus is
   *     found
   */
  public static RequestStatus fromString(String statusStr) {
    for (RequestStatus status : RequestStatus.values()) {
      if (status.toString().equalsIgnoreCase(statusStr)) {
        return status;
      }
    }
    return null;
  }
}
