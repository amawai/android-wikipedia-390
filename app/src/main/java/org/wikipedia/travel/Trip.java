package org.wikipedia.travel;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.wikipedia.json.annotations.Required;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//This class constitutes a basic Trip class with a list of destinations (also implemented as an inner class below)
//and a departure date
public class Trip {

    @SuppressWarnings("unused") @Nullable private List<Destination> destinations;
    @SuppressWarnings("unused") @Nullable private Date departureDate;

    //Parametrized constructor for a trip
    public Trip(List<Destination> destinations, Date departureDate) {
        this.destinations = destinations;
        this.departureDate = departureDate;
    }

    //Getter method for the list of destinations that the trip will entail
    @NonNull
    public List<Destination> getTripDestinations() {
        return (this.destinations != null) ? this.destinations : Collections.emptyList();
    }

    //Method to add a destination to the list of destinations that the trip will entail
    public void addDestination(Destination destination) {
        if (destination != null) {
            destinations.add(destination);
        }
        else
            System.out.println("The destination object could not be added.");
    }

    //Getter method for the trip's departure date
    public Date getTripDepartureDate() {
        if (departureDate == null)
            System.out.println("No date has been set.");
        return this.departureDate;
    }

    //Setter method for the trip's departure date
    public void setTripDepartureDate(Date desiredTripDepartureDate) {
        if (departureDate != null)
            this.departureDate = desiredTripDepartureDate;
        else
            System.out.println("A valid depature date has not been passed.");
    }

    //Checking if the trip actually has destinations
    public boolean areThereDestinationsSelectedForTrip() {
        return (!(this.destinations.isEmpty()));
    }

    //Inner Destination Class
    public static class Destination {
        @SuppressWarnings("unused,NullableProblems") @Required @NonNull private List<Location> placesToVisit;
        @SuppressWarnings("unused,NullableProblems") @Required @NonNull private String destinationName;

        //Parametrized constructor for a destination
        public Destination(List<Location> placesToVisit, String destinationName) {
            this.placesToVisit = placesToVisit;
            this.destinationName = destinationName;
        }

        //Getter method for the list of attractive locations that a destination has
        @NonNull
        public List<Location> getDestinationPlacesToVisit() {
            return (this.placesToVisit != null) ? this.placesToVisit : Collections.emptyList();
        }

        //Method to add a location to the list of location that a destination has
        public void addPlaceToVisit(Location location) {
            if (location != null) {
                placesToVisit.add(location);
            }
            else
                System.out.println("The place to visit object could not be added.");
        }

        //Getter method for the trip's departure date
        @NonNull
        public String getDestinationName() {
            return this.destinationName;
        }

        //Setter method for the trip's destination name
        public void setDestinationName(@NonNull String destinationName) {
            this.destinationName = destinationName;
        }

        //Checking if the destination actually has places to visit
        public boolean areTherePlacesToVisitForDestination() {
            return (!(this.placesToVisit.isEmpty()));
        }
    }
}
