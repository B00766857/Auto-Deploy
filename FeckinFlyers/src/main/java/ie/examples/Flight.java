// B01234567
package ie.examples;

import java.sql.Time;

class Flight
{
    private Long id;
    private Time departureTime;
    private Time arrivalTime;
    private String departureAirport;
    private String arrivalAirport;

    Flight(Long id, Time departureTime, Time arrivalTime, String departureAirport, String arrivalAirport)
    {
        this.id = id;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

	public Time getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

	public Time getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Time arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}