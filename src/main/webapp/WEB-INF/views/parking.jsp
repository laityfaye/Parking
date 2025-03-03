<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gestion du Parking</title>
    <style>
        /* Reset des styles par défaut */
        body, h1, p, form, input, button {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        /* Style général */
        body {
            background-color: #f4f4f4;
            color: #333;
            padding: 20px;
        }

        header {
            text-align: center;
            margin-bottom: 20px;
        }

        main {
            max-width: 800px;
            margin: 0 auto;
        }

        #parking-info {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        #parking-lot {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .parking-spaces {
            display: grid;
            grid-template-columns: repeat(5, 1fr); /* 5 places par ligne */
            gap: 10px;
        }

        .parking-space {
            background-color: #e0e0e0;
            padding: 10px;
            border-radius: 4px;
            text-align: center;
        }

        .parking-space.occupied {
            background-color: #ff6b6b;
            color: white;
        }

        #parking-controls {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        form {
            margin-bottom: 10px;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    <header>
        <h1>Gestion du Parking</h1>
    </header>

    <main>
        <!-- Section pour afficher les informations du parking -->
        <section id="parking-info">
            <p>Nombre total de places : <span id="total-spaces">10</span></p>
            <p>Places disponibles : <span id="available-spaces">10</span></p>
            <p>Places occupées : <span id="occupied-spaces">0</span></p>
        </section>

        <!-- Section pour représenter visuellement les places de parking -->
        <section id="parking-lot">
            <div class="parking-spaces">
                <!-- Les places de parking seront générées dynamiquement en JavaScript -->
            </div>
        </section>

        <!-- Formulaire pour ajouter ou retirer une voiture -->
        <section id="parking-controls">
            <form id="add-car-form">
                <label for="license-plate">Plaque d'immatriculation :</label>
                <input type="text" id="license-plate" name="license-plate" required>
                <button type="submit">Ajouter une voiture</button>
            </form>

            <form id="remove-car-form">
                <label for="remove-license-plate">Plaque d'immatriculation :</label>
                <input type="text" id="remove-license-plate" name="remove-license-plate" required>
                <button type="submit">Retirer une voiture</button>
            </form>
        </section>

        <!-- Affichage des messages dynamiques -->
        <c:if test="${not empty message}">
            <p>${message}</p>
        </c:if>
    </main>

    <script>
        // Données du parking
        let totalSpaces = 10;
        let availableSpaces = 10;
        let occupiedSpaces = 0;
        let cars = [];

        // Éléments du DOM
        const totalSpacesElement = document.getElementById("total-spaces");
        const availableSpacesElement = document.getElementById("available-spaces");
        const occupiedSpacesElement = document.getElementById("occupied-spaces");
        const parkingSpacesElement = document.querySelector(".parking-spaces");
        const addCarForm = document.getElementById("add-car-form");
        const removeCarForm = document.getElementById("remove-car-form");

        // Mettre à jour l'affichage des places de parking
        function updateParkingDisplay() {
            // Mettre à jour les informations
            totalSpacesElement.textContent = totalSpaces;
            availableSpacesElement.textContent = availableSpaces;
            occupiedSpacesElement.textContent = occupiedSpaces;

            // Générer les places de parking
            parkingSpacesElement.innerHTML = "";
            for (let i = 0; i < totalSpaces; i++) {
                const space = document.createElement("div");
                space.classList.add("parking-space");
                if (cars[i]) {
                    space.classList.add("occupied");
                    space.textContent = cars[i];
                } else {
                    space.textContent = "Libre";
                }
                parkingSpacesElement.appendChild(space);
            }
        }

        // Ajouter une voiture
        addCarForm.addEventListener("submit", function (event) {
            event.preventDefault();
            const licensePlate = document.getElementById("license-plate").value;

            // Vérifier si la plaque d'immatriculation est déjà présente
            if (cars.includes(licensePlate)) {
                alert("Cette voiture est déjà dans le parking !");
            } else if (availableSpaces > 0) {
                cars.push(licensePlate);
                availableSpaces--;
                occupiedSpaces++;
                updateParkingDisplay();
                alert(`Voiture ${licensePlate} ajoutée avec succès !`);
            } else {
                alert("Parking plein !");
            }

            addCarForm.reset();
        });

        // Retirer une voiture
        removeCarForm.addEventListener("submit", function (event) {
            event.preventDefault();
            const licensePlate = document.getElementById("remove-license-plate").value;
            const index = cars.indexOf(licensePlate);

            if (index !== -1) {
                cars.splice(index, 1);
                availableSpaces++;
                occupiedSpaces--;
                updateParkingDisplay();
                alert(`Voiture ${licensePlate} retirée avec succès !`);
            } else {
                alert("Voiture non trouvée !");
            }

            removeCarForm.reset();
        });

        // Initialiser l'affichage
        updateParkingDisplay();
    </script>
</body>
</html>