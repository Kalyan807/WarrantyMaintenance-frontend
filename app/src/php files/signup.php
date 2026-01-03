<?php
// signup.php - User Registration API
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Accept");

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(["status" => "error", "message" => "POST method required"]);
    exit;
}

// Database connection
$host = "localhost";
$user = "root";
$pass = "";
$db = "warrantymaintenance";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Database connection failed"]);
    exit;
}

// Read input JSON
$data = json_decode(file_get_contents("php://input"), true);

$full_name = trim($data["full_name"] ?? '');
$email = trim($data["email"] ?? '');
$phone = trim($data["phone"] ?? '');
$address = trim($data["address"] ?? '');
$password = $data["password"] ?? '';
$confirm_password = $data["confirm_password"] ?? '';

// Validate required fields
if (empty($full_name) || empty($email) || empty($phone) || empty($address) || empty($password) || empty($confirm_password)) {
    echo json_encode(["status" => "error", "message" => "All fields are required"]);
    exit;
}

// Validate email format
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo json_encode(["status" => "error", "message" => "Invalid email format"]);
    exit;
}

// Password match validation
if ($password !== $confirm_password) {
    echo json_encode(["status" => "error", "message" => "Passwords do not match"]);
    exit;
}

// Password length validation
if (strlen($password) < 6) {
    echo json_encode(["status" => "error", "message" => "Password must be at least 6 characters"]);
    exit;
}

// Check if email already exists
$check = $conn->prepare("SELECT id FROM users WHERE email = ?");
$check->bind_param("s", $email);
$check->execute();
$result = $check->get_result();

if ($result->num_rows > 0) {
    echo json_encode(["status" => "error", "message" => "Email already registered"]);
    exit;
}

// Hash password
$hashed_password = password_hash($password, PASSWORD_DEFAULT);

// Insert user
$stmt = $conn->prepare("INSERT INTO users (full_name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("sssss", $full_name, $email, $phone, $address, $hashed_password);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "success",
        "message" => "Registration successful",
        "user_id" => $stmt->insert_id
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Registration failed"]);
}

$conn->close();
?>