package com.ivansison.kairos

import com.ivansison.kairos.models.Coordinates
import com.ivansison.kairos.models.Location
import com.ivansison.kairos.utils.ValidateUtil
import org.junit.Test

class RecentSearchesModuleTest {

    @Test
    fun is_Location_Valid() {
        val parameter = "Makati"
        assert(ValidateUtil.isValidLocation(parameter))
    }

    @Test
    fun is_Most_Recent_Search() {
        val locations: ArrayList<Location> = ArrayList()
        locations.add(Location(0, "PH", "Bataan", "", Coordinates(12.0, 150.1)))
        locations.add(Location(0, "PH", "Mandaluyong", "", Coordinates(133.12, 11.1)))
        locations.add(Location(0, "PH", "Makati", "", Coordinates(15.0, 141.1)))

        val location: Location = Location(0, "PH", "Pampanga", "", Coordinates(20.0, 159.23))

        locations.add(0, location)

        assert(locations[0] == location)
    }

    @Test
    fun has_Searched_Location_Already() {
        val locations: ArrayList<Location> = ArrayList()
        locations.add(Location(0, "PH", "Bataan", "", Coordinates(12.0, 150.1)))
        locations.add(Location(0, "PH", "Makati", "", Coordinates(15.0, 141.1)))
        locations.add(Location(0, "PH", "Pampanga", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Mandaluyong", "", Coordinates(20.12, 159.23)))

        val newLocation: Location = Location(0, "PH", "Makati", "", Coordinates(15.0, 141.1))

        var hasLocation = false

        for (savedLocation in locations) {
            if (savedLocation.country == newLocation.country && savedLocation.address1 == newLocation.address1) {
                hasLocation = true
                break
            }
        }

        assert(hasLocation)
    }

    @Test
    fun is_Current_Location() {
        val currentLocation: Location = Location(0, "PH", "Orion", "", Coordinates(15.0, 141.1))
        val newLocation: Location = Location(0, "PH", "Orion", "", Coordinates(15.0, 141.1))

        assert(currentLocation.country == newLocation.country && currentLocation.address1 == newLocation.address1)
    }

    @Test
    fun can_Add_To_Recent_Search() {
        val maxSearch: Int = 15
        val locations: ArrayList<Location> = ArrayList()
        locations.add(Location(0, "PH", "Bataan", "", Coordinates(12.0, 150.1)))
        locations.add(Location(0, "PH", "Makati", "", Coordinates(15.0, 141.1)))
        locations.add(Location(0, "PH", "Pampanga", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Mandaluyong", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Batangas", "", Coordinates(12.0, 150.1)))
        locations.add(Location(0, "PH", "Batanes", "", Coordinates(15.0, 141.1)))
        locations.add(Location(0, "PH", "Palawan", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Bohol", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Cebu", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Lamao", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Limay", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Mariveles", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Orani", "", Coordinates(20.12, 159.23)))
        locations.add(Location(0, "PH", "Shaw", "", Coordinates(20.12, 159.23)))

        val newLocation: Location = Location(0, "PH", "Makati", "", Coordinates(15.0, 141.1))

        assert(locations.size < maxSearch)
    }
}