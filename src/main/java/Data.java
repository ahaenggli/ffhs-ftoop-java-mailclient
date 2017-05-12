
class Data {
  private String a;

  private String b;

  private String c;

  private Boolean d;

  private Integer e;

  public Data() {
  }

  public Data(String aa, String bb, String cc, Boolean dd, Integer ee) {
    a = aa;
    b = bb;
    c = cc;
    d = dd;
    e = ee;
  }

  public String getA() {
    return a;
  }

  public String getB() {
    return b;
  }

  public String getC() {
    return c;
  }

  public Boolean getD() {
    return d;
  }

  public Integer getE() {
    return e;
  }

  public void setA(String aa) {
    a = aa;
  }

  public void setB(String macName) {
    b = macName;
  }

  public void setC(String cc) {
    c = cc;
  }

  public void setD(Boolean value) {
    d = value;
  }

  public void setE(Integer ee) {
    e = ee;
  }
}