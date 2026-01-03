<?php
// get_notifications.php - Get User Notifications API
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

$notifications = [];
$id = 1;

// Generate notifications from pending issues
$pendingResult = $conn->query("
    SELECT appliance, issue_description, created_at 
    FROM issues 
    WHERE status = 'Pending' 
    ORDER BY created_at DESC 
    LIMIT 5
");

while ($row = $pendingResult->fetch_assoc()) {
    $createdAt = new DateTime($row['created_at']);
    $now = new DateTime();
    $diff = $now->diff($createdAt);

    $timeAgo = "";
    if ($diff->days > 0) {
        $timeAgo = $diff->days . " day" . ($diff->days > 1 ? "s" : "") . " ago";
    } elseif ($diff->h > 0) {
        $timeAgo = $diff->h . " hour" . ($diff->h > 1 ? "s" : "") . " ago";
    } else {
        $timeAgo = $diff->i . " minute" . ($diff->i > 1 ? "s" : "") . " ago";
    }

    $notifications[] = [
        "id" => $id++,
        "title" => "Issue Pending",
        "message" => $row['appliance'] . ": " . substr($row['issue_description'], 0, 50),
        "time" => $timeAgo,
        "type" => "issue",
        "isRead" => false
    ];
}

// Generate notifications from expiring warranties
$expiryResult = $conn->query("
    SELECT appliance, expiry_date, DATEDIFF(expiry_date, CURDATE()) as days_left
    FROM warranty_records 
    WHERE expiry_date >= CURDATE() 
    AND expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)
    ORDER BY expiry_date ASC
    LIMIT 5
");

while ($row = $expiryResult->fetch_assoc()) {
    $notifications[] = [
        "id" => $id++,
        "title" => "Warranty Expiring",
        "message" => $row['appliance'] . " warranty expires in " . $row['days_left'] . " days",
        "time" => $row['days_left'] . " days left",
        "type" => "warning",
        "isRead" => false
    ];
}

// Generate notifications from resolved issues
$resolvedResult = $conn->query("
    SELECT appliance, updated_at 
    FROM issues 
    WHERE status = 'Resolved' 
    ORDER BY updated_at DESC 
    LIMIT 3
");

while ($row = $resolvedResult->fetch_assoc()) {
    $updatedAt = new DateTime($row['updated_at']);
    $now = new DateTime();
    $diff = $now->diff($updatedAt);

    $timeAgo = "";
    if ($diff->days > 0) {
        $timeAgo = $diff->days . " day" . ($diff->days > 1 ? "s" : "") . " ago";
    } else {
        $timeAgo = $diff->h . " hour" . ($diff->h > 1 ? "s" : "") . " ago";
    }

    $notifications[] = [
        "id" => $id++,
        "title" => "Issue Resolved",
        "message" => $row['appliance'] . " issue has been resolved",
        "time" => $timeAgo,
        "type" => "success",
        "isRead" => true
    ];
}

// Sort by read status (unread first)
usort($notifications, function ($a, $b) {
    return $a['isRead'] - $b['isRead'];
});

// Count unread
$unreadCount = count(array_filter($notifications, function ($n) {
    return !$n['isRead'];
}));

echo json_encode([
    "notifications" => $notifications,
    "unreadCount" => $unreadCount
]);

$conn->close();
?>