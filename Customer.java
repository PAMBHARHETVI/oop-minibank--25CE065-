class Customer implements Cloneable
{
    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    private Address address;

    private static long customerCounter = 100;

    private static String generateCustomerId()
    {
        customerCounter ++;
        return "CUST" + customerCounter;
    }

    Customer(String name, String email , String mobile)
    {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.customerId = generateCustomerId();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public Address getAddress()
    {
        return address;
    }

    public static class Address{

        String line;
        String city;
        String pincode;

        public Address(String line, String city, String pincode)
        {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine()
        {
            return line;
        }

        public String getCity()
        {
            return city;
        }

        public String getpincode()
        {
            return pincode;
        }

        @Override
        public String toString()
        {
            return line + ", " + city + " - " + pincode;
        }
    }

    @Override

    public Customer clone()
    {
        try{
            Customer copy = (Customer)super.clone();
            return copy;
        }
        catch(CloneNotSupportedException e)
        {
            throw new AssertionError();
        }
    }
}

