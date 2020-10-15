public class Driver {

  private String driverID;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String carMaker;
  private String carModel;
  private String carLicence;
  private double rate;

  /**
   * Constructor of Driver Class
   * @param driverID String representing driverID of the object
   * @param firstName String representing the first name of the object
   * @param lastName String representing the last name of the object
   * @param phoneNumber String representing the phone number of the object
   * @param carMaker String representing the car maker of the car the Driver object drives
   * @param carModel String representing the car model of the car the Driver object drives
   * @param carLicence String representing the car licence of the car the Driver object drives
   * @param rate double representing the rating of the object
   */
  public Driver(String driverID, String firstName, String lastName, String phoneNumber,
      String carMaker, String carModel, String carLicence, double rate) {
    this.driverID = driverID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.carMaker = carMaker;
    this.carModel = carModel;
    this.carLicence = carLicence;
    this.rate = rate;
  }



}
