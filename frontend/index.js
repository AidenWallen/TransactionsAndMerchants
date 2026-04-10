const URL = "http://localhost:8080/api/v1/";
let allMerchants = [];
let allTransactions = [];
let merchantTransactions = [];
let transactionMerchants = [];

document.addEventListener("DOMContentLoaded", () => {
    let getMerchants = new XMLHttpRequest();
    let getTransactions = new XMLHttpRequest();

    getMerchants.onreadystatechange = () => {

        if (getMerchants.readyState === 4) {
            let merchants = JSON.parse(getMerchants.responseText);
            merchants.forEach((data) => addMerchantToTable(data));
        }
    }

    getTransactions.onreadystatechange = () => {

        if (getTransactions.readyState === 4) {
            let transactions = JSON.parse(getTransactions.responseText);
            transactions.forEach((data) => addTransactionToTable(data));
        }
    }

    getMerchants.open("GET", URL + "merchants");
    getMerchants.send();

    getTransactions.open("GET", URL + "transactions");
    getTransactions.send();
})

const createMerchant = async ()=>{
    const firstName = document.getElementById("create-first-name").value.trim();
    const lastName = document.getElementById("create-last-name").value.trim();
    const category = document.getElementById("create-category").value;
    const country = document.getElementById("create-country").value.trim();
    const status = document.getElementById("create-status").value;

    if (!firstName || !lastName || !category || !country || !status) {
        alert("All fields must be filled out.");
        return;
    }

    const merchant = {
        firstName,
        lastName,
        category,
        country,
        status
    };

    const response = await fetch(URL + "merchants",{
        method: "POST",
        headers: {
            "Content-Type":"application/json"
        },
        body:JSON.stringify(merchant)
    });

    if(!response.ok)
        throw new Error("Failed to create merchant");

    const createdMerchant = await response.json();

    addMerchantToTable(createdMerchant);
}

const createTransaction = async ()=>{
    const type = document.getElementById("create-transaction-type").value;
    const amount = document.getElementById("create-transaction-amount").value;
    const currency = document.getElementById("create-transaction-currency").value;
    const status = document.getElementById("create-transaction-status").value;
    const date = document.getElementById("create-transaction-date").value;
    const notes = document.getElementById("create-transaction-notes").value;

    if (!type || !amount || !currency || !status) {
        alert("Type, amount, currency, and status must be filled out.");
        return;
    }

    if(amount<=0){
        alert("A transaction must have an amount greater than 0.")
    }

    const transaction = {
        type,
        amount,
        currency,
        status,
        date,
        notes
    };

    const response = await fetch(URL + "transactions",{
        method: "POST",
        headers: {
            "Content-Type":"application/json"
        },
        body:JSON.stringify(transaction)
    });

    if(!response.ok)
        throw new Error("Failed to create transaction");

    const createdTransaction = await response.json();

    addTransactionToTable(createdTransaction);
}

const addMerchantToTable = (newMerchant) => {

    let tr = document.createElement("tr");
    let id = document.createElement("td");
    let firstName = document.createElement("td");
    let lastName = document.createElement("td");
    let transactionCount = document.createElement("td");
    let transactionVolume = document.createElement("td");

    let detailsBtn = document.createElement("td");

    id.innerText = newMerchant.id;
    firstName.innerText = newMerchant.firstName;
    lastName.innerText = newMerchant.lastName;
    transactionCount.innerText = newMerchant.transactionCount;
    transactionVolume.innerText = newMerchant.transactionVolume;

    detailsBtn.innerHTML = `<button class="btn bg-info btn-outline-dark p-1" onclick="activateMerchantDetailsModal(${newMerchant.id})">View</button>`;

    tr.appendChild(id);
    tr.appendChild(firstName);
    tr.appendChild(lastName);
    tr.appendChild(transactionCount);
    tr.appendChild(transactionVolume);

    tr.appendChild(detailsBtn);

    tr.setAttribute("id", "TR-merchant-" + newMerchant.id);
    tr.setAttribute("class", "align-bottom");

    let tableBody = document.getElementById("merchant-table-body");
    tableBody.appendChild(tr);

    allMerchants.push(newMerchant);
}

const addTransactionToTable = (newTransaction) => {

    let tr = document.createElement("tr");
    let id = document.createElement("td");
    let type = document.createElement("td");
    let amount = document.createElement("td");
    let currency = document.createElement("td");
    let date = document.createElement("td");
    let status = document.createElement("td");

    let detailsBtn = document.createElement("td");

    id.innerText = newTransaction.id;
    type.innerText = formatEnum(newTransaction.type);
    amount.innerText = newTransaction.amount;
    currency.innerText = newTransaction.currency;
    date.innerText = new Date(newTransaction.date).toLocaleString();
    status.innerText = formatEnum(newTransaction.status);

    detailsBtn.innerHTML = `<button class="btn bg-info btn-outline-dark p-1" onclick="activateTransactionDetailsModal(${newTransaction.id})">View</button>`;

    tr.appendChild(id);
    tr.appendChild(type);
    tr.appendChild(amount);
    tr.appendChild(currency);
    tr.appendChild(date);
    tr.appendChild(status);

    tr.appendChild(detailsBtn);

    tr.setAttribute("id", "TR-transaction-" + newTransaction.id);
    tr.setAttribute("class", "align-bottom");

    let tableBody = document.getElementById("transaction-table-body");
    tableBody.appendChild(tr);

    allTransactions.push(newTransaction);
}

const addTransactionToMerchantTable = (newTransaction) => {

    let tr = document.createElement("tr");
    let role = document.createElement("td");
    let id = document.createElement("td");
    let type = document.createElement("td");
    let amount = document.createElement("td");
    let currency = document.createElement("td");
    let date = document.createElement("td");
    let status = document.createElement("td");

    let detailsBtn = document.createElement("td");

    role.innerText = formatEnum(newTransaction.role);
    id.innerText = newTransaction.id;
    type.innerText = formatEnum(newTransaction.type);
    amount.innerText = newTransaction.amount;
    currency.innerText = newTransaction.currency;
    date.innerText = new Date(newTransaction.processingDate).toLocaleString();
    status.innerText = formatEnum(newTransaction.status);

    detailsBtn.innerHTML = `<button class="btn bg-info btn-outline-dark p-1" onclick="activateTMDetailsModal(${newTransaction.processingdetails})">View</button>`;

    tr.appendChild(role)
    tr.appendChild(id);
    tr.appendChild(type);
    tr.appendChild(amount);
    tr.appendChild(currency);
    tr.appendChild(date);
    tr.appendChild(status);

    tr.appendChild(detailsBtn);

    tr.setAttribute("id", "TR-tm-transactions-" + newTransaction.id);
    tr.setAttribute("class", "align-bottom");

    let tableBody = document.getElementById("merchant-transactions-table-body");
    tableBody.appendChild(tr);

    merchantTransactions.push(newTransaction);
}

const addMerchantToTransactionTable = (newMerchant) => {

    let tr = document.createElement("tr");
    let role = document.createElement("td");
    let id = document.createElement("td");
    let name = document.createElement("td");
    let category = document.createElement("td");
    let status = document.createElement("td");
    let date = document.createElement("td");

    let detailsBtn = document.createElement("td");

    role.innerText = formatEnum(newMerchant.role);
    id.innerText = newMerchant.id;
    name.innerText = newMerchant.firstName+" "+newMerchant.lastName;
    category.innerText = formatEnum(newMerchant.category);
    status.innerText = formatEnum(newMerchant.status);
    date.innerText = new Date(newMerchant.date).toLocaleString();

    detailsBtn.innerHTML = `<button class="btn bg-info btn-outline-dark p-1" onclick="activateTMDetailsModal(${newMerchant.processingNotes})">View</button>`;

    tr.appendChild(role)
    tr.appendChild(id);
    tr.appendChild(name);
    tr.appendChild(category);
    tr.appendChild(status);
    tr.appendChild(date);

    tr.appendChild(detailsBtn);

    tr.setAttribute("id", "TR-tm-merchants-" + newMerchant.id);
    tr.setAttribute("class", "align-bottom");

    let tableBody = document.getElementById("transaction-merchants-table-body");
    tableBody.appendChild(tr);

    transactionMerchants.push(newMerchant);
}

const activateMerchantDetailsModal = async (merchantId) => {
    const response = await fetch(URL+`merchants/${merchantId}/transactions`);
    if(!response.ok)
        throw new Error("Failed to fetch transactions by merchant");

    const merchantWithTransactions = await response.json();
    const merchant = merchantWithTransactions.merchant;
    const transactions = merchantWithTransactions.transactions;

    document.getElementById("merchant-id").value = merchant.id;
    document.getElementById("merchant-name").value = merchant.firstName+" "+merchant.lastName;
    document.getElementById("merchant-category").value = merchant.category;
    document.getElementById("merchant-country").value = merchant.country;
    document.getElementById("merchant-status").value = merchant.status;

    let tableBody = document.getElementById("merchant-transactions-table-body");
    tableBody.innerHTML = "";
    
    transactions.forEach((transaction)=>addTransactionToMerchantTable(transaction));

    const modal = new bootstrap.Modal(document.getElementById("merchant-modal"));
    modal.show();

}

const activateTransactionDetailsModal = async (transactionId) => {
    const response = await fetch(URL+`transactions/${transactionId}/merchants`);
    if(!response.ok)
        throw new Error("Failed to fetch transactions by merchant");

    const transactionWithMerchants = await response.json();
    const transaction = transactionWithMerchants.transaction;
    const merchants = transactionWithMerchants.merchants;

    document.getElementById("transaction-id").value = transaction.id;
    document.getElementById("transaction-type").value = transaction.type;
    document.getElementById("transaction-amount").value = transaction.amount+" "+transaction.currency;
    document.getElementById("transaction-date").value = new Date(transaction.date).toISOString().split("T")[0];
    document.getElementById("transaction-status").value = transaction.status;
    document.getElementById("transaction-notes").value = transaction.notes;

    let tableBody = document.getElementById("transaction-merchants-table-body");
    tableBody.innerHTML = "";
    
    merchants.forEach((merchant)=>addMerchantToTransactionTable(merchant));

    const modal = new bootstrap.Modal(document.getElementById("transaction-modal"));
    modal.show();

}

function formatEnum(value){
    if (!value) return "";

    return value
        .toLowerCase()
        .split("_")
        .map(word => word.charAt(0).toUpperCase()+word.slice(1))
        .join(" ");
}

const updateMerchant = async ()=>{
    const merchant = {
        id: document.getElementById("merchant-id").value,
        country:document.getElementById("merchant-country").value,
        status: document.getElementById("merchant-status").value
    }

    const response = await fetch(URL+`merchants/${merchant.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(merchant)
    });

    if (!response.ok) {
        throw new Error("Failed to update merchant");
    }

    loadMerchants();

    const modal = bootstrap.Modal.getInstance(document.getElementById("merchant-modal"));
    modal.hide();

}

const loadMerchants = async ()=>{
    const response = await fetch(URL+"merchants");
    const merchants = await response.json();

    document.getElementById("merchant-table-body").innerHTML = "";
    allMerchants = [];

    merchants.forEach(m => addMerchantToTable(m));
}

const updateTransaction = async ()=>{

    const id = document.getElementById("transaction-id").value;
    const status = document.getElementById("transaction-status").value;
    const notes = document.getElementById("transaction-notes").value;
    

    const params = new URLSearchParams({
        status:status,
        notes:notes
    })

    const response = await fetch(URL+`transactions/${id}?${params.toString()}`, {
        method: "PUT"
    });

    if (!response.ok) {
        throw new Error("Failed to update merchant");
    }

    loadTransactions();

    const modal = bootstrap.Modal.getInstance(document.getElementById("transaction-modal"));
    modal.hide();
}

const loadTransactions = async ()=>{
    const response = await fetch(URL+"transactions");
    const transactions = await response.json();

    document.getElementById("transaction-table-body").innerHTML = "";
    allTransactions = [];

    transactions.forEach(t => addTransactionToTable(t));
}

let deleteMerchantId = null;

const confirmMerchantDelete = () => {
    deleteMerchantId = document.getElementById("merchant-id").value;

    const modal = new bootstrap.Modal(
        document.getElementById("confirm-delete-merchant-modal")
    );
    modal.show();
};

const deleteMerchant = async () => {
    const response = await fetch(URL+`merchants/${deleteMerchantId}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Failed to delete merchant");
    }

    await loadMerchants();

    bootstrap.Modal.getInstance(
        document.getElementById("confirm-delete-merchant-modal")
    ).hide();

    bootstrap.Modal.getInstance(
        document.getElementById("merchant-modal")
    ).hide();
};

let deleteTransactionId = null;

const confirmTransactionDelete = () => {
    deleteTransactionId = document.getElementById("transaction-id").value;

    const modal = new bootstrap.Modal(
        document.getElementById("confirm-delete-transaction-modal")
    );
    modal.show();
};

const deleteTransaction = async () => {
    const response = await fetch(URL+`transactions/${deleteTransactionId}`, {
        method: "DELETE"
    });

    if (!response.ok) {
        throw new Error("Failed to delete Transaction");
    }

    await loadTransactions();

    bootstrap.Modal.getInstance(
        document.getElementById("confirm-delete-transaction-modal")
    ).hide();

    bootstrap.Modal.getInstance(
        document.getElementById("transaction-modal")
    ).hide();
};

let reverseTransactionId = null;
let reverseTransactionNotes = null;

const startTransactionReverse = () => {
    reverseTransactionId = document.getElementById("transaction-id").value;
    reverseTransactionNotes = document.getElementById("transaction-notes").value;

    const modal = new bootstrap.Modal(
        document.getElementById("confirm-reverse-transaction-modal")
    );
    modal.show();
};

const confirmTransactionReverse = () => {
    const type = document.getElementById("transaction-reverse-type").value;

    if(!type){
        alert("Please select a type")
        return;
    }

    reverseTransaction(type);
}

const reverseTransaction = async (type) => {
    const params = new URLSearchParams();

    params.append("reverseType", type);
    if (reverseTransactionNotes) params.append("notes", reverseTransactionNotes);

    const response = await fetch(URL+`transactions/${reverseTransactionId}?${params.toString()}`,{
        method: "POST"
    });

    if (!response.ok) {
        throw new Error("Failed to reverse transaction");
    }

    const createdTransaction = await response.json();
    addTransactionToTable(createdTransaction);

    bootstrap.Modal.getInstance(
        document.getElementById("confirm-reverse-transaction-modal")
    ).hide();

    bootstrap.Modal.getInstance(
        document.getElementById("transaction-modal")
    ).hide();
};

const applyTransactionFilters = async ()=>{
    const id = document.getElementById("filter-id").value;
    if(id){
        const response = await fetch(URL+`transactions/${id}`);
        if(!response.ok)
            throw new Error("Failed to fetch transaction by id");
        const transaction = await response.json();

        document.getElementById("transaction-table-body").innerHTML = "";

        addTransactionToTable(transaction);
        return;
    }

    const type = document.getElementById("filter-type").value;
    const status = document.getElementById("filter-status").value;
    const start = document.getElementById("filter-start-date").value;
    const end = document.getElementById("filter-end-date").value;

    const params = new URLSearchParams();

    if (type) params.append("type", type);
    if (status) params.append("status", status);
    if (start) params.append("start", start + "T00:00:00Z");
    if (end) params.append("end", end + "T23:59:59Z");

    const response = await fetch(URL+`transactions/filter?${params.toString()}`);

    if (!response.ok) {
        throw new Error("Failed to filter transactions");
    }

    const transactions = await response.json();
    document.getElementById("transaction-table-body").innerHTML = "";

    transactions.forEach(t => addTransactionToTable(t));
}

const activateTMDetailsModal = ()=>{
    const merchantModalEl = document.getElementById("merchant-modal");
    const merchantModal = bootstrap.Modal.getInstance(merchantModalEl);

    if (merchantModal) {
        merchantModal.hide();
    }

    const tmModalEl = document.getElementById("tm-modal");
    const tmModal = new bootstrap.Modal(tmModalEl);

    tmModal.show();
}

const linkTransactionMerchant = async () => {
    const merchantId = document.getElementById("link-merchant-id").value;
    const transactionId = document.getElementById("link-transaction-id").value;
    const role = document.getElementById("link-role").value;
    const date = document.getElementById("link-date").value;
    const notes = document.getElementById("link-notes").value;

    if (!merchantId || !transactionId || !role){
        alert("Merchant ID, Transacton ID, and Role fields must be filled out.")
        return;
    }

    const body={
        merchantId,
        transactionId,
        role,
        date,
        notes
    }

    const response = await fetch(URL+`transaction-merchants`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) 
        alert("Failed to link. Relationship already exists.")
};

const editTransactionMerchant = async () => {
    const merchantId = document.getElementById("link-merchant-id").value;
    const transactionId = document.getElementById("link-transaction-id").value;
    const role = document.getElementById("link-role").value;
    const date = document.getElementById("link-date").value;
    const notes = document.getElementById("link-notes").value;

    if (!merchantId || !transactionId || !role){
        alert("Merchant ID, Transacton ID, and Role fields must be filled out.")
        return;
    }

    const body = {
        merchantId,
        transactionId,
        role,
        date,
        notes
    };

    const response = await fetch(URL+`transaction-merchants`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) 
        throw new Error("Failed to update");
};

const deleteTransactionMerchant = async () => {
    const merchantId = document.getElementById("link-merchant-id").value;
    const transactionId = document.getElementById("link-transaction-id").value;
    const role = document.getElementById("link-role").value;

    if (!merchantId || !transactionId || !role){
        alert("Merchant ID, Transacton ID, and Role fields must be filled out.")
        return;
    }

    const body = {
        merchantId,
        transactionId,
        role
    };

    const response = await fetch(URL+`transaction-merchants`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (!response.ok) 
        throw new Error("Failed to delete");
};