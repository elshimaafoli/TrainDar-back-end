package com.appuser.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {
        String regex="^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        CharSequence input;
        Matcher matcher= pattern.matcher(email);
        return matcher.matches();
    }
}
