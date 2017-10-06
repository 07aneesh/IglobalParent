package com.cts.cheetah.helpers;

import android.os.Environment;

import java.io.File;

public final class ApplicationRef {
	public static final String APP_NAME="IGLOBALS SCHOOL";
	public static final String TRUE="true";
	public static final String OK="ok";
	public static final String CANCEL="cancel";
	public static final String FROM_INTENT="fromIntent";



	private ApplicationRef(){

	}

	public static final class AppConfig {
		public static final String BASE_URL="base_url";
		public static final String API_VERSION = "api_version";
		public static final String ALLOW_APP_ACCESS= "allow_app_access";
		public static final String STORE_URL="store_url";
	}
	public static final class AppConstants {
		public static final String ADD_NEW="Add New";
		public static final String CAMERA="Camera";
		public static final String GALLERY="Gallery";

		public static final String APP_IMAGE_FOLDER= Environment.getExternalStorageDirectory()+ File.separator+"Pictures"+ File.separator+APP_NAME;
		public static final String APP_THUMB_FOLDER= Environment.getExternalStorageDirectory()+ File.separator+"Pictures"+ File.separator+APP_NAME+ File.separator+"thumbs";

		private AppConstants(){

		}
	}

	public static final class Common {
		public static final String TYPE="Type";
	}

	public static final class Amazon {

		public static final String S3_URL = "http://cheetahstage.s3.amazonaws.com/Images/";//http://cheetahdocs.s3.amazonaws.com/Images/
		public static final String DOC_S3_URL = "http://cheetahstage.s3.amazonaws.com/Documents/Driver/";
		public static final String BUCKET_NAME="cheetahstage";//cheetahdocs
		public static final String IMAGE_SUB_FOLDER="Images/";
		public static final String DOC_IMAGE_SUB_FOLDER="Documents/Driver/";
		public static final String THUMB_IMAGE_SUB_FOLDER="Thumbs/";
		public static final String COGNITO_POOL_ID="us-west-2:adaefdf9-abc2-4d5a-9e22-31a01888aca3";

		public static final String COGNITO_POOL_REGION = "CHANGE_ME";
		public static final String BUCKET_REGION = "us-west-2";
	}

	public static final class Earnings {
		public static final String EARNING_DAILY = "daily";
		public static final String EARNING_WEEKLY = "weekly";
		public static final String DAY_OF_WEEK = "day_of_week";
	}

	public static final class Records {
		public static final String LICENSE = "License";
		public static final String MEDICAL = "Medical";
	}


	public static final class DBConstants {
		public static final String UPDATE = "update";
		public static final String INSERT = "insert";
	}

	public static final class GCMConstants {
		public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
		public static final String REGISTRATION_COMPLETE = "registrationComplete";
		private GCMConstants(){

		}
	}

	public static final class Service {
		public static final String CONFIG_BASE_URL="https://app.reachoutsuite.com/app/server/public/getConfig?mobile_platform=android&app_version=5";
		//public static final String CONFIG_BASE_URL = "https://app.com.reachout.corelib.model.com/app/server/public/getConfig?mobile_platform=android&app_version=4";
		//public static final String CONFIG_BASE_URL = "http://app.reachout.fingent.net/app/server/public/getConfig?mobile_platform=android&app_version=2";
		//public static final String CONFIG_BASE_URL = "https://beta.app.reachoutsuite.com/app/server/public/getConfig?mobile_platform=android&app_version=2";
		public static final int GET=0;
		public static final int POST=1;
		public static final String BEARER ="Bearer ";//End space is required
		public static final String CLIENT="client";
		public static final String EMAIL_KEY="email";
		public static final String P_KEY ="password";

		public static final String LOGIN_SUCCESS="loginSuccess";
		public static final String LOGIN_FAIL="loginFail";
		public static final String STATUS_CODE_SUCCESS="200";
		public static final String CONFIG="config";
		public static final String USER_ID_KEY="user_id";
		public static final String OLD_P_KEY ="oldpassword";
		public static final String NEW_P_KEY ="newpassword";
		public static final String FORM_ID_KEY="form_id";
		public static final String TOKEN_KEY="token";
		public static final String ID="id";
		public static final String DEVICE_ID="device_id";
		public static final String DEVICE_TYPE="device_type";
		public static final String RESPONSE="response";
		public static final String OTP_KEY="otp";

		public static final String API="api";
		public static final String ISSUE="issue";
		public static final String CANCEL="cancel";
		public static final String STATUS_MSG="statusMessage";
		public static final String STATUS="status";
		public static final String STATUS_COMPLETED="Completed";
		public static final String STATUS_SUCCESS="success";
		public static final String STATUS_FAILED="failed";
		public static final String SERVICE_TIME_OUT="serviceTimeout";
		public static final String INACTIVE_SESSION="Inactive Session";
		public static final String STATUS_DUPLICATE_LOGIN="duplicate_login";
		public static final String ACTION="action";
		public static final String DATA="data";
		public static final String LOGIN_ACTION="Login";
		public static final String LOGOUT_ACTION="Logout";
		public static final String GET_STATUS_CODES="GetApplicationStatusCodes";
		public static final String FORCE_LOGOUT_ACTION="forceLogout";
		public static final String FORGOT_PASSWORD ="ForgotPassword";
		public static final String GET_DRIVER_ACCOUNT ="GetDriverAccount";
		public static final String SET_DRIVER_AVAILABILITY ="SetAvailability";
		public static final String GET_PUSH_NOTIFICATIONS ="GetPushNotifications";
		public static final String GET_DRIVER_LICENSE ="GetDriverLicense";
		public static final String GET_DRIVER_MEDICAL ="GetDriverMedical";
		public static final String GET_DRIVER_SAFETY_SCORE ="GetDriverSafetyScore";
		public static final String UPDATE_DRIVER_LICENSE ="UpdateDriverLicense";
		public static final String UPDATE_DRIVER_MEDICAL ="UpdateDriverMedical";
		public static final String ADD_DRIVER_MEDICAL ="AddDriverMedical";
		public static final String GET_ISSUE_REASON_LIST ="GetIssueReasonList";
		public static final String GET_TRIP_REQUEST ="GetTripRequest";
		public static final String ACCEPT_CANCEL_TRIP_REQUEST ="AcceptCancelTripRequest";
		public static final String UPDATE_MY_TRIP_STATUS ="UpdateMyTripStatus";
		public static final String UPDATE_MY_TRIP ="UpdateMyTrip";
		public static final String UPDATE_DETENTION_TIME ="UpdateDetentionTime";
		public static final String UPDATE_MY_TRIP_COMPLETION ="UpdateTripCompletion";
		public static final String ADD_DRIVER_LOCATION ="AddDriverLocation";
		public static final String GET_COMPLETED_TRIPS ="GetCompletedTrips";
		public static final String GET_PAYOUTS ="GetPayouts";
		public static final String GET_EARNINGS ="GetEarnings";
		public static final String GET_MY_TRIPS ="GetMyTrips";
		public static final String GET_MY_TRIP_SINGLE ="GetSingleTripByOrder";
		public static final String GET_ACCESSORIALS ="GetAccessorials";
		public static final String ADD_TRIP_SIGNATURE ="AddTripSignature";
		public static final String INVALID_CODE ="401";
		public static final String CHANGE_P_ACTION ="changePassword";
		public static final String SEND_DEVICE_ID_ACTION="sendDeviceId";
		public static final String UPLOAD_IMAGE="uploadImage";
		//SIGNATURE
		public static final String CUSTOMER_NAME="customerName";
		public static final String SIGNATURE_FILE="signatureFile";
		public static final String SIGNATURE_FILE_NAME="signatureFileName";

		public static final String SUBMIT_LOG_ACTION="submitLog";
		public static final String LOGS="logs";

		//For iglobal php site by an
		public static final String PHP_GET_APLN_STATUS_CODES="AplnConfigurations.php?apicall=GetApplicationStatusCodes";
		public static final String PHP_LOGIN_ACTION="MobParentLogin.php?apicall=Login";
		public static final String PHP_Student_Account_Details="MobParentLogin.php?apicall=StudentDetails";
		public static final String PHP_Student_DAILY_TRIP="StudentTripDetails.php?apicall=GetStudentDailyTrip";
		public static final String PHP_Student_TRIP_GPS="StudentTripDetails.php?apicall=GetStudentTripLatitudeLongitude";
		private Service(){

		}
	}



	public static final class Users {
		public static final String USER_NAME="UserName";
		private Users(){

		}
	}
	public static final class Config{
		public static final String BASE_URL="baseUrl";
		private Config(){

		}
	}

	public static final class LoginTags {
		public static final String DRIVER_ID="driverId";
		public static final String DRIVER_FIRST_NAME ="firstName";
		public static final String DRIVER_LAST_NAME ="lastName";
		public static final String DRIVER_EMAIL ="email";
		public static final String USER_NAME_TAG="username";
		public static final String ACCESS_TOKEN_TAG="accessToken";
		public static final String DRIVER_AVAILABILITY_CODE="availabilityCode";
		public static final String DRIVER_AVAILABILITY_STATUS="availability";
		public static final String USER_PREFERENCE_FILE="User_Preference_File";
		private LoginTags(){

		}
	}

	public static final class StatusCodes {
		public static final String STATUS_CODE = "statusCode";
		public static final String DRIVER_STATUS = "driverAvaialbilty";
		public static final String  VEHICLE_SERVICE_STATUS = "vehicleServiceStatus";
		public static final String  VEHICLE_TYPE = "vehicleType";
		public static final String  DOCUMENT_STATUS = "documentStatus";
		//
		public static final String DRIVER_ACCEPTANCE_STATUS = "driverAcceptanceStatus";
		public static final String DRIVER_AVAILABILITY_ONLINE = "Online";
		public static final String DRIVER_AVAILABILITY_OFFLINE="Offline";
		//
		public static final String DRIVER_ACCEPTANCE_ACCEPTED = "Accepted";
		public static final String DRIVER_ACCEPTANCE_REJECTED="Rejected";
		public static final String DRIVER_ACCEPTANCE_TIMEOUT="Timeout";
		//
		public static final String VEHICLE_SERVICE_STATUS_PLANNED="Planned";
		public static final String VEHICLE_SERVICE_STATUS_AVAILABLE="Available";
		public static final String VEHICLE_SERVICE_STATUS_OUT_OF_SERVICE="Out of Service";
		public static final String VEHICLE_SERVICE_STATUS_TREMINATED="Terminated";
		//
		public static final String VEHICLE_TYPE_PICKUP="Pickup";
		public static final String VEHICLE_TYPE_PICKUP_WITH_RACK="Pickup with Rack";
		public static final String VEHICLE_TYPE_SMALL_STAKE_BED="Small StakeBed";
		public static final String VEHICLE_TYPE_LARGE_STAKE_BED="Large StakeBed";
		public static final String VEHICLE_TYPE_TRACTOR_TRAILOR="Tractor Trailier";
		public static final String VEHICLE_TYPE_MINI_FLOAT="Mini-Float";
		//
		public static final String DOCUMENT_STATUS_APPROVED="Approved";
		public static final String DOCUMENT_STATUS_PENDING_APPROVAL="Pending Approval";
		public static final String DOCUMENT_STATUS_EXPIRED="Expired";
		public static final String DOCUMENT_STATUS_REJECTED="Rejected";
		public static final String DOCUMENT_STATUS_NO_RECORD="No Record";
		//
		public static final String DRIVER_AVAILABILTY_STATUS="driverAvailabiltyStatus";
		public static final String DRIVER_STATUS_CODES="driverStatusCodes";
		public static final String VEHICLE_STATUS_CODES="vehicleStatusCodes";
		public static final String VEHICLE_TYPE_CODES="vehicleTypeCodes";
		public static final String DOCUMENT_STATUS_CODES="documentStatusCodes";
		//LOCATION STATUS
		public static final String LOCATION_STATUS_PENDING="Pending";
		public static final String LOCATION_STATUS_PROCESSED="Processed";
		public static final String LOCATION_STATUS_ARRIVED="Arrived";

		//ORDER STATUS
		public static final String ORDER_STATUS="orderStatus";
		public static final String ORDER_STATUS_ORDER_PLACED="Order Placed";
		public static final String ORDER_STATUS_DRAFT="Draft";
		public static final String ORDER_STATUS_WAITING="Waiting for Dispatch";
		public static final String ORDER_STATUS_DISPATCHED="Dispatched";
		public static final String ORDER_STATUS_AT_PICKUP="At Pickup";
		public static final String ORDER_STATUS_AT_DROPOFF="At Drop Off";
		public static final String ORDER_STATUS_IN_TRANSIT="In Transit";
		public static final String ORDER_STATUS_COMPLETED="Completed";
		public static final String ORDER_STATUS_ON_HOLD="On Hold";
		public static final String ORDER_STATUS_AT_DRYRUN="Dry Run";
		public static final String ORDER_STATUS_IN_DETENTION="Detention";
		public static final String ORDER_STATUS_CANCELLED="Cancelled";
		public static final String ORDER_STATUS_DELETED="Deleted";

		private StatusCodes(){

		}
	}

	public static final class TripTags {
		public static final String TRIP_ID="trip_id";
		public static final String TRIP_CONTROL_NO="ControlNo";
		public static final String LATITUDE="latitude";
		public static final String LONGITUDE="longitude";
		public static final String LOCATION_ID="locationId";
		public static final String LOCATION_TYPE="location_type";
		public static final String LOCATION_NO="location_no";
		public static final String LOCATION_ORDER="locationOrder";
		public static final String LOCATION_TYPE_PICK_UP="PickUp";
		public static final String LOCATION_TYPE_DROP_OFF="DropOff";
		public static final String LOCATION_TYPE_ROUND_TRIP="RoundTrip";
		public static final String ACCESSORIALS="Accessorials";
		public static final String BASE_PAY="BasePay";
		public static final String NOTES="Notes";
		public static final String SAFETY_INSTRUCTIONS="Safety Instructions";
		public static final String IS_LAST_LOCATION="isLastLocation";
		public static final String IS_DETENTION_STARTED="isDetentionStarted";
		public static final String IS_ROUND_TRIP="isRoundTrip";
		public static final String DETENTION_TIME="detentionTime";
		public static final String PROXIMITY_REACHED="proximityReached";


		private TripTags(){

		}
	}

	public static final class System{
		public static final String INVALID_EMAIL="Invalid Email Address";
		public static final String INPUT_USER="Please Enter User Id";
		public static final String INPUT_P ="Please Enter Password";
		private System(){

		}

	}

	public static final class Preferences {
		public static final String UPLOAD_WHILE_WIFI_ONLY="upload_while_wifi_only";
		public static final String UPLOAD_WHILE_CHARGING_ONLY="upload_while_charging_only";
		private Preferences(){

		}

	}

	public static final class Document{
		public static final String APPROVED="Approved";
		public static final String PENDING_APPROVAL="Pending Approval";
		public static final String REJECTED="Rejected";
		public static final String EXPIRED ="Expired";
		public static final String LICENSE="License";
		public static final String MEDICAL="Medical";
		public static final String SAFETY_SCORE="SafetyScore";

		private Document(){

		}

	}

}
