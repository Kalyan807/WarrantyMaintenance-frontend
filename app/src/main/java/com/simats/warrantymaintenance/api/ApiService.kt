package com.simats.warrantymaintenance.api

import com.simats.warrantymaintenance.data.AddTechnicianResponse
import com.simats.warrantymaintenance.data.AddWarrantyResponse
import com.simats.warrantymaintenance.data.AnalyticsData
import com.simats.warrantymaintenance.data.AppliancesResponse
import com.simats.warrantymaintenance.data.AssignTaskRequest
import com.simats.warrantymaintenance.data.AssignTaskResponse
import com.simats.warrantymaintenance.data.DashboardSummary
import com.simats.warrantymaintenance.data.ForgotPasswordRequest
import com.simats.warrantymaintenance.data.ForgotPasswordResponse
import com.simats.warrantymaintenance.data.IssuesResponse
import com.simats.warrantymaintenance.data.LoginRequest
import com.simats.warrantymaintenance.data.LoginResponse
import com.simats.warrantymaintenance.data.NotificationsResponse
import com.simats.warrantymaintenance.data.ReportIssueResponse
import com.simats.warrantymaintenance.data.ServiceHistoryResponse
import com.simats.warrantymaintenance.data.ServiceTrackingResponse
import com.simats.warrantymaintenance.data.SignupRequest
import com.simats.warrantymaintenance.data.SignupResponse
import com.simats.warrantymaintenance.data.TaskDetails
import com.simats.warrantymaintenance.data.TechnicianDashboardData
import com.simats.warrantymaintenance.data.TechnicianProfile
import com.simats.warrantymaintenance.data.TechniciansResponse
import com.simats.warrantymaintenance.data.WarrantyExpiryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("signup.php")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

    @POST("forgot_password.php")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @GET("dashboard_summary.php")
    fun getDashboardSummary(): Call<DashboardSummary>

    @Multipart
    @POST("add_technician.php")
    fun addTechnician(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("specialization") specialization: RequestBody,
        @Part("address") address: RequestBody,
        @Part idProof: MultipartBody.Part
    ): Call<AddTechnicianResponse>

    @Multipart
    @POST("add_warranty_record.php")
    fun addWarranty(
        @Part("appliance") appliance: RequestBody,
        @Part("model_number") modelNumber: RequestBody,
        @Part("purchase_date") purchaseDate: RequestBody,
        @Part("expiry_date") expiryDate: RequestBody,
        @Part("maintenance_frequency") maintenanceFrequency: RequestBody,
        @Part("notes") notes: RequestBody,
        @Part document: MultipartBody.Part
    ): Call<AddWarrantyResponse>

    @Multipart
    @POST("report_issue.php")
    fun reportIssue(
        @Part("appliance") appliance: RequestBody,
        @Part("description") description: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ReportIssueResponse>

    @POST("update_issue_status.php")
    fun assignTask(@Body request: AssignTaskRequest): Call<AssignTaskResponse>

    @GET("get_issues.php")
    fun getIssues(): Call<IssuesResponse>

    @GET("get_analytics.php")
    fun getAnalyticsData(): Call<AnalyticsData>

    @GET("get_technicians.php")
    fun getTechnicians(): Call<TechniciansResponse>

    @GET("get_warranty_expiry.php")
    fun getWarrantyExpiry(): Call<WarrantyExpiryResponse>

    @GET("get_technician_dashboard.php")
    fun getTechnicianDashboardData(): Call<TechnicianDashboardData>

    @GET("get_service_history.php")
    fun getServiceHistory(): Call<ServiceHistoryResponse>

    @GET("get_notifications.php")
    fun getNotifications(): Call<NotificationsResponse>

    @GET("get_technician_profile.php")
    fun getTechnicianProfile(): Call<TechnicianProfile>

    @GET("get_user_appliances.php")
    fun getUserAppliances(): Call<AppliancesResponse>

    @GET("get_service_tracking.php")
    fun getServiceTracking(@Query("user_id") userId: Int): Call<ServiceTrackingResponse>

    @GET("get_task_details.php")
    fun getTaskDetails(@Query("task_id") taskId: Int): Call<TaskDetails>
}