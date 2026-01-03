<?php
// dashboard_summary.php - Supervisor Dashboard Summary API
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Accept");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    echo json_encode(["status" => "error", "message" => "GET method required"]);
    exit;
}

// Database connection
$conn = new mysqli("localhost", "root", "", "warrantymaintenance");
if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Database connection failed"]);
    exit;
}

// Get total appliances (from warranty_records)
$appliancesResult = $conn->query("SELECT COUNT(*) as count FROM warranty_records");
$totalAppliances = $appliancesResult->fetch_assoc()['count'];

// Get total technicians
$techniciansResult = $conn->query("SELECT COUNT(*) as count FROM technicians");
$totalTechnicians = $techniciansResult->fetch_assoc()['count'];

// Get pending issues
$pendingResult = $conn->query("SELECT COUNT(*) as count FROM issues WHERE status = 'Pending'");
$pendingIssues = $pendingResult->fetch_assoc()['count'];

// Get warranties expiring in next 30 days
$expiryResult = $conn->query("
    SELECT COUNT(*) as count FROM warranty_records 
    WHERE expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
");
$warrantyExpiry = $expiryResult->fetch_assoc()['count'];

echo json_encode([
    "totalAppliances" => (int) $totalAppliances,
    "totalTechnicians" => (int) $totalTechnicians,
    "pendingIssues" => (int) $pendingIssues,
    "warrantyExpiry" => (int) $warrantyExpiry
]);

$conn->close();
?>