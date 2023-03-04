package com.android.starchat.ui.uiStart;

import com.android.starchat.util.Sort;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Locale;
import java.util.Set;

public class RegionHelper {


    public static Country[] createCountries(StartActivityViewModel viewModel){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Set<String>regionSet = phoneUtil.getSupportedRegions();
        Country[] countries = new Country[regionSet.size()];
        int i= 0;
        for (String region : regionSet){
            Locale locale = new Locale("",region);
            String name = locale.getDisplayName();
            String code = String.valueOf(phoneUtil.getCountryCodeForRegion(region));
            Country country = new Country(name,code,region);
            country.setAction(new Country.Action() {
                @Override
                public void setCountry() {
                    viewModel.setSelectedCountry(country);
                }
            });
            countries[i]=country;
            i++;
        }
        countries = Sort.mergeSortNonRecursive(countries);
        return countries;
    }
}
