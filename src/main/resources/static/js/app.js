document.addEventListener("DOMContentLoaded", () => {
    loadExpenses();
});

async function loadExpenses() {

    const tableBody = document.getElementById("expenseTableBody");

    try {

        const response = await fetch("/api/expenses");

        if (!response.ok) {
            throw new Error("Failed to fetch expenses");
        }

        const expenses = await response.json();

        tableBody.innerHTML = "";

        if (expenses.length === 0) {

            tableBody.innerHTML = `
                <tr>
                    <td colspan="5"
                        class="text-center text-secondary">
                        No expenses available
                    </td>
                </tr>
            `;

            return;
        }

        expenses.forEach(expense => {

            const row = `
                <tr>

                    <td>
                        ${expense.date}
                    </td>

                    <td>
                        ${expense.category}
                    </td>

                    <td>
                        ${expense.description}
                    </td>

                    <td>
                        ₹${Number(expense.amount).toLocaleString("en-IN")}
                    </td>

                    <td>

                        <button
                            class="btn btn-sm btn-warning">
                            Edit
                        </button>

                        <button
                            class="btn btn-sm btn-danger">
                            Delete
                        </button>

                    </td>

                </tr>
            `;

            tableBody.innerHTML += row;

        });

    }
    catch (error) {

        console.error(error);

        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="text-center text-danger">

                    Failed to load expenses

                </td>
            </tr>
        `;

    }

}