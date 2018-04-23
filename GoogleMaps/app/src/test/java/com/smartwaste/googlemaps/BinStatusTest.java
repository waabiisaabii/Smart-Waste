package com.smartwaste.googlemaps;

import org.junit.Test;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yachen on 3/25/18.
 */
public class BinStatusTest {
    @Test
    public void testNormalBeharior() throws MalformedURLException {
        BinStatus binStatus = new BinStatus("https://peaceful-island-64716.herokuapp.com/iot/returnJSON");
        List<BinStatus.Item> items = binStatus.doInBackground();
        assertTrue(items.size() != 0);
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURL() throws MalformedURLException {
        BinStatus binStatus = new BinStatus("testInvalidURL");
    }


}