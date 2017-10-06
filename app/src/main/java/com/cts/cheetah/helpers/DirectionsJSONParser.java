package com.cts.cheetah.helpers;

/**
 * Created by manu.palassery on 24-02-2017.
 */

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser {

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<List<HashMap<String,String>>> parse(JSONObject jObject){

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    String jsonDirection = "{\n" +
            "   \"geocoded_waypoints\" : [\n" +
            "      {\n" +
            "         \"geocoder_status\" : \"OK\",\n" +
            "         \"place_id\" : \"ChIJbwq8CIIMCDsRy6yXsjqKZ8o\",\n" +
            "         \"types\" : [ \"establishment\", \"point_of_interest\" ]\n" +
            "      },\n" +
            "      {\n" +
            "         \"geocoder_status\" : \"OK\",\n" +
            "         \"place_id\" : \"ChIJbQUSrYR0CDsRD4UAemb_T7Q\",\n" +
            "         \"types\" : [ \"locality\", \"political\" ]\n" +
            "      }\n" +
            "   ],\n" +
            "   \"routes\" : [\n" +
            "      {\n" +
            "         \"bounds\" : {\n" +
            "            \"northeast\" : {\n" +
            "               \"lat\" : 10.0146732,\n" +
            "               \"lng\" : 76.3667287\n" +
            "            },\n" +
            "            \"southwest\" : {\n" +
            "               \"lat\" : 9.946930699999999,\n" +
            "               \"lng\" : 76.351382\n" +
            "            }\n" +
            "         },\n" +
            "         \"copyrights\" : \"Map data ©2017 Google\",\n" +
            "         \"legs\" : [\n" +
            "            {\n" +
            "               \"distance\" : {\n" +
            "                  \"text\" : \"9.5 km\",\n" +
            "                  \"value\" : 9535\n" +
            "               },\n" +
            "               \"duration\" : {\n" +
            "                  \"text\" : \"20 mins\",\n" +
            "                  \"value\" : 1171\n" +
            "               },\n" +
            "               \"end_address\" : \"Thiruvankulam, Kerala, India\",\n" +
            "               \"end_location\" : {\n" +
            "                  \"lat\" : 9.946930699999999,\n" +
            "                  \"lng\" : 76.3667287\n" +
            "               },\n" +
            "               \"start_address\" : \"Carnival Phase IV Exit Road, Infopark Campus, Kakkanad, Kerala 682030, India\",\n" +
            "               \"start_location\" : {\n" +
            "                  \"lat\" : 10.0144203,\n" +
            "                  \"lng\" : 76.3633706\n" +
            "               },\n" +
            "               \"steps\" : [\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"71 m\",\n" +
            "                        \"value\" : 71\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"1 min\",\n" +
            "                        \"value\" : 20\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 10.0145454,\n" +
            "                        \"lng\" : 76.3638507\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Head \\u003cb\\u003eeast\\u003c/b\\u003e on \\u003cb\\u003eCarnival Phase IV Exit Rd\\u003c/b\\u003e toward \\u003cb\\u003eCarnival Phase IV Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eRestricted usage road\\u003c/div\\u003e\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"c}b|@avaqMAKEQCMEQA??@A?A?A?A?A??AA?A?AAA??AA??A?AA??A?A?AA??A?A@??A?A?A@??A?A@??A@?@A@??A@?@?@?\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 10.0144203,\n" +
            "                        \"lng\" : 76.3633706\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"39 m\",\n" +
            "                        \"value\" : 39\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"1 min\",\n" +
            "                        \"value\" : 14\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 10.0146753,\n" +
            "                        \"lng\" : 76.36417949999999\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e toward \\u003cb\\u003eInfopark Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eRestricted usage road\\u003c/div\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-left\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"}}b|@ayaqMGMEIIg@AA\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 10.0145454,\n" +
            "                        \"lng\" : 76.3638507\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"0.5 km\",\n" +
            "                        \"value\" : 468\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"1 min\",\n" +
            "                        \"value\" : 78\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 10.0108892,\n" +
            "                        \"lng\" : 76.3660024\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e at the 1st cross street onto \\u003cb\\u003eInfopark Rd\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Wipro Technologies (on the right)\\u003c/div\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-right\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"w~b|@c{aqMTOVE|A[f@I^IZMlAk@XQTKxFgC~@c@lAa@\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 10.0146753,\n" +
            "                        \"lng\" : 76.36417949999999\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"0.3 km\",\n" +
            "                        \"value\" : 271\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"1 min\",\n" +
            "                        \"value\" : 77\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 10.0099812,\n" +
            "                        \"lng\" : 76.3638424\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eInfopark Expy\\u003c/b\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-right\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"agb|@ofbqMNIDR^tAxBtH@??@@??@@??@@??@?@?@@??@?@?@A??@?@?@A??@\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 10.0108892,\n" +
            "                        \"lng\" : 76.3660024\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"2.4 km\",\n" +
            "                        \"value\" : 2386\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"4 mins\",\n" +
            "                        \"value\" : 250\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 9.996260399999999,\n" +
            "                        \"lng\" : 76.351382\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e to stay on \\u003cb\\u003eInfopark Expy\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Infopark (on the right)\\u003c/div\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-left\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"kab|@_yaqMPf@Nd@HPJTXTJFNDJDVDXDTDnEx@RHPFTJTHRLVNPRRTRXFJLNJJJHLHPFJDH@NBF?F?L?RCVCZG^I~@Kv@GhAGt@C|AEd@Af@Ab@@X@z@Hz@J|@Rv@T`@NXL\\\\Pb@Th@\\\\d@^|@t@fDzCZ^\\\\XtNhLv@|@@?@?@??@@?@@@??@@@?@@??@?@?@?@?@?@^j@Rf@LZ`@lAJ\\\\DTF^Dh@LpAF~@@j@BdC?JAt@EjAUdDG~AArA\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 10.0099812,\n" +
            "                        \"lng\" : 76.3638424\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"4.4 km\",\n" +
            "                        \"value\" : 4412\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"8 mins\",\n" +
            "                        \"value\" : 477\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 9.9576282,\n" +
            "                        \"lng\" : 76.3584365\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e after Infopark Entrance Bldg (on the right)\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Trans Asia Corporate Park (on the left)\\u003c/div\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-left\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"sk_|@ck_qM|@G|@Ej@Cj@Gr@Cn@Ep@Eh@EVCx@En@GpBQpBQ`F[h@Ct@ENC`@G^GTCRE^El@Gr@CjAIr@CZAT@R@H?`@DF@F@z@PxA\\\\VFZHVFRBNBJ@L@VCVA`@EfAEJAj@Ah@Cj@Ah@Cb@Cb@Cn@Ef@Gb@Ib@GdAQfAOnBUhAK^CpCUz@In@GlBSt@Gv@It@GXE\\\\C~@Kl@Gf@ITE\\\\I~@UfBi@RGx@WPGd@Ot@UXIXIt@ULEf@Mx@U~@Y`A[nCcAnBy@pAg@HCjA_@t@U^ITGXGPCREd@C^C`@C`@C\\\\CdAKfBS`@E^Ev@KbAMz@M|AUtAWpA[`AWj@SRCBAJE^KLETEPCb@E`@Ah@Ch@Ah@Aj@Cj@Cf@CB?n@Eb@A|@I|AKnAI`@ErD_@fE[`@Cb@A`@C`@Cd@Cd@CLAHAJAnBQpBOjCM|@I\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 9.996260399999999,\n" +
            "                        \"lng\" : 76.351382\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"0.7 km\",\n" +
            "                        \"value\" : 719\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"2 mins\",\n" +
            "                        \"value\" : 96\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 9.9511641,\n" +
            "                        \"lng\" : 76.3584699\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"At \\u003cb\\u003eIrumpanam Jct\\u003c/b\\u003e, continue onto \\u003cb\\u003eNH85\\u003c/b\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Karur Vysya Bank ATM (on the right in 650&nbsp;m)\\u003c/div\\u003e\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"ezw{@gw`qMfECbF@LAnC?r@CpBAbCCrB?^?dGF\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 9.9576282,\n" +
            "                        \"lng\" : 76.3584365\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  },\n" +
            "                  {\n" +
            "                     \"distance\" : {\n" +
            "                        \"text\" : \"1.2 km\",\n" +
            "                        \"value\" : 1169\n" +
            "                     },\n" +
            "                     \"duration\" : {\n" +
            "                        \"text\" : \"3 mins\",\n" +
            "                        \"value\" : 159\n" +
            "                     },\n" +
            "                     \"end_location\" : {\n" +
            "                        \"lat\" : 9.946930699999999,\n" +
            "                        \"lng\" : 76.3667287\n" +
            "                     },\n" +
            "                     \"html_instructions\" : \"Turn \\u003cb\\u003eleft\\u003c/b\\u003e after \\u003cb\\u003eKaringachira Jct\\u003c/b\\u003e (on the left)\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003ePass by Punjab National Bank ATM (on the right in 350&nbsp;m)\\u003c/div\\u003e\\u003cdiv style=\\\"font-size:0.9em\\\"\\u003eDestination will be on the right\\u003c/div\\u003e\",\n" +
            "                     \"maneuver\" : \"turn-left\",\n" +
            "                     \"polyline\" : {\n" +
            "                        \"points\" : \"wqv{@mw`qMOs@W_A_@eA]iAI]G]Ek@Cg@E}@Ek@?_@?]Bk@BO@_@@M@MBKDSd@eAj@eA\\\\u@LWHMLMJI^KZGt@ORGTIb@Qj@]ROf@_@NMh@c@xBcBPQf@]l@c@@?XWLINONOLQHKFMTe@Ra@P]\\\\i@Xa@\"\n" +
            "                     },\n" +
            "                     \"start_location\" : {\n" +
            "                        \"lat\" : 9.9511641,\n" +
            "                        \"lng\" : 76.3584699\n" +
            "                     },\n" +
            "                     \"travel_mode\" : \"DRIVING\"\n" +
            "                  }\n" +
            "               ],\n" +
            "               \"traffic_speed_entry\" : [],\n" +
            "               \"via_waypoint\" : []\n" +
            "            }\n" +
            "         ],\n" +
            "         \"overview_polyline\" : {\n" +
            "            \"points\" : \"c}b|@avaqMQ}@A?E@ICEG?KDGDC@?EMOq@RQ|Du@hBy@n@]xHkD|Ak@~C~K@@B@BHAHA@j@~AJTXTZLrAVnEx@RHf@Rh@Vh@b@|@jAVT^Pl@J`AGz@QvBSbGSjA?tAJz@J|@RxAd@v@^lAr@bBtAfDzCZ^rObMx@|@@?B@DF@F?@^l@`@bAl@jBLt@RzBHjBBpCG`CUdDG~AArA|@GhBI~AKbDUlIq@pIi@jBYlAM~BMnAEh@Bz@HhE`AfAPd@Ax@GhDM|CMvAMfAQlCa@xDa@|Gk@vLkA|@O|A_@fEqAtHyB`Cu@~F}BzAk@`Cu@t@Qj@Kx@I`AG~@GlD_@|De@xCc@fDs@dCq@nA]t@IjAEjDK~BKlGe@rD_@fE[dAEnCOrCW|F]|@IfECpF?bECtFErC?dGFOs@w@eCg@gBMiAIeBEkABiADo@B[H_@pAkCj@mAV[JI^KpAWh@QnAo@z@o@dEgDpByA\\\\Y\\\\a@z@aBhAiB\"\n" +
            "         },\n" +
            "         \"summary\" : \"Infopark Expy and Seaport - Airport Rd\",\n" +
            "         \"warnings\" : [],\n" +
            "         \"waypoint_order\" : []\n" +
            "      }\n" +
            "   ],\n" +
            "   \"status\" : \"OK\"\n" +
            "}";

    public JSONObject getDirectionData(){

        JSONObject json = null;
        try {
            json = new JSONObject(jsonDirection);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}