package com.android.starchat.util;
import com.android.starchat.ui.uiStart.Country;
import com.android.starchat.ui.uiStart.RegionHelper;
import com.android.starchat.ui.uiStart.StartActivityViewModel;

import org.junit.Test;

public class TestRegion {



    @Test
    public void testCountries(){
        Country[] countries = RegionHelper.createCountries(new StartActivityViewModel());
        for (int i=0; i<countries.length; i++){
            System.out.println(countries[i].getName()+" "+countries[i].getCode()+" "+countries[i].getRegion());
        }
    }
}
