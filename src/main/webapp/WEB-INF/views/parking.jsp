<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion du Parking</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #3498db;
            --secondary-color: #2c3e50;
            --success-color: #2ecc71;
            --danger-color: #e74c3c;
            --light-color: #ecf0f1;
            --dark-color: #34495e;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #f5f7fa;
            color: #333;
            line-height: 1.6;
        }

        header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 1.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-bottom: 2rem;
        }

        header h1 {
            font-size: 2.5rem;
            font-weight: 600;
        }

        main {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 1.5rem 3rem;
        }

        section {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
            padding: 1.5rem;
            margin-bottom: 2rem;
        }

        h2 {
            color: var(--secondary-color);
            margin-bottom: 1.2rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--light-color);
        }

        /* Messages d'alerte */
        .message {
            padding: 15px;
            margin: 0 0 2rem;
            border-radius: 8px;
            display: flex;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .message::before {
            font-family: 'Font Awesome 6 Free';
            font-weight: 900;
            margin-right: 12px;
            font-size: 1.2rem;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border-left: 5px solid #28a745;
        }

        .success::before {
            content: "\f058"; /* check-circle icon */
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border-left: 5px solid #dc3545;
        }

        .error::before {
            content: "\f057"; /* times-circle icon */
        }

        /* Informations du parking */
        #parking-info {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            text-align: center;
        }

        #parking-info p {
            flex: 1;
            min-width: 200px;
            padding: 1rem;
            margin: 0.5rem;
            border-radius: 8px;
            background-color: var(--light-color);
            font-size: 1.1rem;
        }

        #parking-info span {
            display: block;
            font-size: 2.5rem;
            font-weight: bold;
            margin-top: 0.5rem;
            color: var(--secondary-color);
        }

        #parking-info p:nth-child(2) span {
            color: var(--success-color);
        }

        #parking-info p:nth-child(3) span {
            color: var(--danger-color);
        }

        /* Représentation visuelle du parking */
        .parking-spaces {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 15px;
            margin-top: 1.5rem;
        }

        .parking-space {
            background-color: var(--light-color);
            border-radius: 6px;
            padding: 1rem;
            text-align: center;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
            height: 100px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        }

        .parking-space::before {
            font-family: 'Font Awesome 6 Free';
            font-weight: 900;
            font-size: 1.8rem;
            margin-bottom: 0.5rem;
            color: var(--secondary-color);
            content: "\f540"; /* parking icon */
        }

        .parking-space.occupied {
            background-color: rgba(231, 76, 60, 0.15);
            border: 1px solid var(--danger-color);
        }

        .parking-space.occupied::before {
            content: "\f1b9"; /* car icon */
            color: var(--danger-color);
        }

        /* Liste des voitures */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            border-radius: 8px;
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
        }

        th {
            background-color: var(--primary-color);
            color: white;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.9rem;
            letter-spacing: 0.5px;
        }

        tr:nth-child(even) {
            background-color: rgba(236, 240, 241, 0.5);
        }

        tbody tr:hover {
            background-color: rgba(52, 152, 219, 0.1);
        }

        /* Formulaires */
        #parking-controls {
            display: flex;
            flex-direction: column;
        }

        .controls-container {
            display: flex;
            flex-wrap: wrap;
            gap: 2rem;
            margin-top: 1rem;
        }

        form {
            flex: 1;
            min-width: 300px;
            background-color: var(--light-color);
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        }

        label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: var(--secondary-color);
        }

        input[type="text"] {
            width: 100%;
            padding: 10px 12px;
            border: 2px solid #ddd;
            border-radius: 6px;
            font-size: 1rem;
            margin-bottom: 1rem;
            transition: border-color 0.3s;
        }

        input[type="text"]:focus {
            border-color: var(--primary-color);
            outline: none;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
        }

        button {
            background-color: var(--primary-color);
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-size: 1rem;
            font-weight: 600;
            width: 100%;
            transition: background-color 0.3s, transform 0.2s;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        button:hover {
            background-color: #2980b9;
            transform: translateY(-2px);
        }

        button:active {
            transform: translateY(0);
        }

        button[type="submit"][name="action"][value="enter"]::before {
            content: "\f090"; /* sign-in icon */
            font-family: 'Font Awesome 6 Free';
            font-weight: 900;
            margin-right: 8px;
        }

        button[type="submit"][name="action"][value="exit"]::before {
            content: "\f08b"; /* sign-out icon */
            font-family: 'Font Awesome 6 Free';
            font-weight: 900;
            margin-right: 8px;
        }

        /* Responsive */
        @media (max-width: 768px) {
            #parking-info {
                flex-direction: column;
            }
            
            .parking-spaces {
                grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
            }
            
            th, td {
                padding: 8px 10px;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
    <header>
        <h1><i class="fas fa-parking"></i> Gestion du Parking</h1>
    </header>

    <main>
        <!-- Message d'alerte -->
        <c:if test="${not empty message}">
            <div class="message ${message.contains('succès') ? 'success' : 'error'}">
                ${message}
            </div>
        </c:if>

        <!-- Section pour afficher les informations du parking -->
        <section id="parking-info">
            <p>
                <i class="fas fa-car-side"></i> Nombre total de places
                <span>${totalSpaces}</span>
            </p>
            <p>
                <i class="fas fa-check-circle"></i> Places disponibles
                <span>${availableSpaces}</span>
            </p>
            <p>
                <i class="fas fa-car"></i> Places occupées
                <span>${occupiedSpaces}</span>
            </p>
        </section>

        <!-- Section pour représenter visuellement les places de parking -->
        <section id="parking-lot">
            <h2><i class="fas fa-map-marker-alt"></i> État du parking</h2>
            <div class="parking-spaces">
                <c:forEach begin="1" end="${totalSpaces}" varStatus="loop">
                    <c:set var="isOccupied" value="false" />
                    <c:set var="carInfo" value="" />
                    
                    <c:forEach items="${cars}" var="car" varStatus="carStatus">
                        <c:if test="${carStatus.index + 1 == loop.index}">
                            <c:set var="isOccupied" value="true" />
                            <c:set var="carInfo" value="${car.licensePlate}" />
                        </c:if>
                    </c:forEach>
                    
                    <div class="parking-space ${isOccupied ? 'occupied' : ''}">
                        Place ${loop.index}
                        <div>${isOccupied ? carInfo : 'Libre'}</div>
                    </div>
                </c:forEach>
            </div>
        </section>

        <!-- Liste des voitures dans le parking -->
        <section id="cars-list">
            <h2><i class="fas fa-list"></i> Voitures actuellement dans le parking</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Plaque d'immatriculation</th>
                        <th>Heure d'entrée</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cars}" var="car">
                        <tr>
                            <td>${car.id}</td>
                            <td>${car.licensePlate}</td>
                            <td>${car.formattedEntryTime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>

        <!-- Formulaire pour ajouter ou retirer une voiture -->
        <section id="parking-controls">
            <h2><i class="fas fa-cogs"></i> Contrôles</h2>
            
            <div class="controls-container">
                <form action="${pageContext.request.contextPath}/add-car" method="post">
                    <input type="hidden" name="action" value="enter">
                    <label for="license-plate-enter">Plaque d'immatriculation :</label>
                    <input type="text" id="license-plate-enter" name="licensePlate" placeholder="TH_3453" required>
                    <button type="submit">Faire entrer une voiture</button>
                </form>

                <form action="${pageContext.request.contextPath}/remove-car" method="post">
                    <input type="hidden" name="action" value="exit">
                    <label for="license-plate-exit">Plaque d'immatriculation :</label>
                    <input type="text" id="license-plate-exit" name="licensePlate" placeholder="TH_3453" required>
                    <button type="submit">Faire sortir une voiture</button>
                </form>
            </div>
        </section>
        <section id="parking-logs">
            <h2><i class="fas fa-history"></i> Historique de Stationnement</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Plaque d'immatriculation</th>
                        <th>Heure d'entrée</th>
                        <th>Heure de sortie</th>
                        <th>Durée (minutes)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty parkingLogs}">
                        <c:forEach items="${parkingLogs}" var="log">
                            <tr>
                                <td>${log.id}</td>
                                <td>${log.licensePlate}</td>
                                <td>
                                    <fmt:parseDate value="${log.entryTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedEntryTime" type="both" />
                                    <fmt:formatDate value="${parsedEntryTime}" pattern="dd/MM/yyyy HH:mm:ss" />
                                </td>
                                <td>
                                    <fmt:parseDate value="${log.exitTime}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedExitTime" type="both" />
                                    <fmt:formatDate value="${parsedExitTime}" pattern="dd/MM/yyyy HH:mm:ss" />
                                </td>
                                <td>${log.durationMinutes}</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty parkingLogs}">
                        <tr>
                            <td colspan="5" style="text-align: center;">Aucun historique de stationnement</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </section>
    </main>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/js/all.min.js"></script>
</body>
</html>
