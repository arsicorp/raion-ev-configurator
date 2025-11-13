// ============================================
// RAION MOTORS - CONFIGURATOR JAVASCRIPT
// Professional Tesla/Apple-Inspired Logic
// ============================================

// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Application State
const state = {
    selectedLevel: null,
    selectedTrim: null,
    selectedColor: null,
    currentView: 'front',
    selectedOptions: [],
    selectedAccessories: [],
    vehicleData: null,
    signatureVehicles: [],
    currentSignature: null
};

// ============================================
// INITIALIZATION
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    initializeTabNavigation();
    initializeViewSwitcher();
    initializeModelSelection();
    initializePaymentCalculator();
    initializeSignatureModels();
    
    console.log('Raion Configurator initialized');
});

// ============================================
// TAB NAVIGATION
// ============================================

function initializeTabNavigation() {
    const tabButtons = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabButtons.forEach(button => {
        button.addEventListener('click', () => {
            const tabName = button.getAttribute('data-tab');
            
            // Update active states
            tabButtons.forEach(btn => btn.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));
            
            button.classList.add('active');
            document.getElementById(tabName).classList.add('active');
        });
    });
}

// ============================================
// VIEW SWITCHER (4 VIEWS)
// ============================================

function initializeViewSwitcher() {
    // Custom build view switcher
    const viewButtons = document.querySelectorAll('[data-view]');
    viewButtons.forEach(button => {
        button.addEventListener('click', () => {
            const view = button.getAttribute('data-view');
            state.currentView = view;
            
            // Update active button
            viewButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            
            // Update image
            updateVehicleImage();
        });
    });
    
    // Signature view switcher
    const sigViewButtons = document.querySelectorAll('[data-sig-view]');
    sigViewButtons.forEach(button => {
        button.addEventListener('click', () => {
            const view = button.getAttribute('data-sig-view');
            
            // Update active button
            sigViewButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            
            // Update signature image
            updateSignatureImage(view);
        });
    });
}

// ============================================
// MODEL SELECTION
// ============================================

function initializeModelSelection() {
    const modelCards = document.querySelectorAll('.model-card');
    
    modelCards.forEach(card => {
        card.addEventListener('click', async () => {
            const level = parseInt(card.getAttribute('data-level'));
            
            // Update state
            state.selectedLevel = level;
            state.selectedTrim = null;
            state.selectedColor = null;
            state.selectedOptions = [];
            
            // Update UI
            modelCards.forEach(c => c.classList.remove('selected'));
            card.classList.add('selected');
            
            // Load trims and colors for this level
            await loadTrimsForLevel(level);
            await loadColorsForLevel(level);
            await loadOptionsForLevel(level);
            
            // Show configuration sections
            document.getElementById('trim-section').style.display = 'block';
            document.getElementById('color-section').style.display = 'block';
            document.getElementById('options-section').style.display = 'block';
            document.getElementById('accessories-section').style.display = 'block';
        });
    });
}

// ============================================
// API CALLS
// ============================================

async function loadTrimsForLevel(level) {
    try {
        const response = await fetch(`${API_BASE_URL}/vehicles/${level}/trims`);
        const data = await response.json();
        
        const trimOptions = document.getElementById('trim-options');
        trimOptions.innerHTML = '';
        
        data.trims.forEach(trim => {
            const trimDiv = document.createElement('div');
            trimDiv.className = 'trim-option';
            trimDiv.innerHTML = `
                <div>
                    <div class="trim-name">${trim.name}</div>
                    <div class="trim-specs">${trim.power} hp • 0-60 in ${trim.acceleration}s</div>
                </div>
                <div class="trim-price">$${formatPrice(trim.price)}</div>
            `;
            
            trimDiv.addEventListener('click', () => {
                state.selectedTrim = trim.code;
                document.querySelectorAll('.trim-option').forEach(t => t.classList.remove('selected'));
                trimDiv.classList.add('selected');
                
                updatePricing();
                updateVehicleSpecs();
                updateVehicleImage();
                showSummaryCards();
            });
            
            trimOptions.appendChild(trimDiv);
        });
    } catch (error) {
        console.error('Error loading trims:', error);
    }
}

async function loadColorsForLevel(level) {
    try {
        const response = await fetch(`${API_BASE_URL}/vehicles/${level}/colors`);
        const data = await response.json();
        
        const colorOptions = document.getElementById('color-options');
        colorOptions.innerHTML = '';
        
        data.colors.forEach(color => {
            const colorDiv = document.createElement('div');
            colorDiv.className = 'color-option';
            colorDiv.innerHTML = `
                <div class="color-swatch" style="background-color: ${color.hex}"></div>
                <div class="color-name">${color.name}</div>
            `;
            
            colorDiv.addEventListener('click', () => {
                state.selectedColor = color.code;
                document.querySelectorAll('.color-option').forEach(c => c.classList.remove('selected'));
                colorDiv.classList.add('selected');
                
                updateVehicleImage();
                document.getElementById('place-order-btn').style.display = 'block';
            });
            
            colorOptions.appendChild(colorDiv);
        });
    } catch (error) {
        console.error('Error loading colors:', error);
    }
}

async function loadOptionsForLevel(level) {
    try {
        const response = await fetch(`${API_BASE_URL}/vehicles/${level}/options`);
        const data = await response.json();
        
        const optionsList = document.getElementById('options-list');
        optionsList.innerHTML = '';
        
        data.options.forEach(option => {
            const optionLabel = document.createElement('label');
            optionLabel.className = 'checkbox-option';
            optionLabel.innerHTML = `
                <input type="checkbox" name="option" value="${option.name.toLowerCase().replace(/\s+/g, '-')}" data-price="${option.price}">
                <span class="option-content">
                    <span class="option-name">${option.name}</span>
                    <span class="option-price">+$${formatPrice(option.price)}</span>
                </span>
            `;
            
            const checkbox = optionLabel.querySelector('input');
            checkbox.addEventListener('change', () => {
                updateSelectedOptions();
                updatePricing();
            });
            
            optionsList.appendChild(optionLabel);
        });
    } catch (error) {
        console.error('Error loading options:', error);
    }
}

// ============================================
// PRICING CALCULATIONS
// ============================================

function updateSelectedOptions() {
    const optionCheckboxes = document.querySelectorAll('input[name="option"]:checked');
    state.selectedOptions = Array.from(optionCheckboxes).map(cb => ({
        name: cb.value,
        price: parseFloat(cb.getAttribute('data-price'))
    }));
}

function updateSelectedAccessories() {
    const accessoryCheckboxes = document.querySelectorAll('input[name="accessory"]:checked');
    state.selectedAccessories = Array.from(accessoryCheckboxes).map(cb => ({
        name: cb.value,
        price: parseFloat(cb.getAttribute('data-price'))
    }));
}

function updatePricing() {
    if (!state.selectedLevel || !state.selectedTrim) {
        return;
    }
    
    // Get base price from selected trim
    const trimElement = document.querySelector('.trim-option.selected .trim-price');
    const basePrice = trimElement ? parseFloat(trimElement.textContent.replace(/[$,]/g, '')) : 0;
    
    // Calculate options total
    updateSelectedOptions();
    const optionsTotal = state.selectedOptions.reduce((sum, opt) => sum + opt.price, 0);
    
    // Calculate accessories total
    updateSelectedAccessories();
    const accessoriesTotal = state.selectedAccessories.reduce((sum, acc) => sum + acc.price, 0);
    
    // Calculate subtotal
    const subtotal = basePrice + optionsTotal + accessoriesTotal;
    
    // Calculate tax (8.5%)
    const tax = subtotal * 0.085;
    
    // Calculate total
    const total = subtotal + tax;
    
    // Update UI
    document.getElementById('base-price').textContent = '$' + formatPrice(basePrice);
    document.getElementById('options-price').textContent = '$' + formatPrice(optionsTotal);
    document.getElementById('accessories-price').textContent = '$' + formatPrice(accessoriesTotal);
    document.getElementById('subtotal-price').textContent = '$' + formatPrice(subtotal);
    document.getElementById('tax-price').textContent = '$' + formatPrice(tax);
    document.getElementById('total-price').textContent = '$' + formatPrice(total);
    
    // Update payment calculator
    updateMonthlyPayment();
}

function formatPrice(price) {
    return price.toLocaleString('en-US', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
}

// ============================================
// VEHICLE SPECS & IMAGE
// ============================================

async function updateVehicleSpecs() {
    if (!state.selectedLevel) return;
    
    try {
        const response = await fetch(`${API_BASE_URL}/vehicles/${state.selectedLevel}/trims`);
        const data = await response.json();
        
        const selectedTrimData = data.trims.find(t => t.code === state.selectedTrim);
        if (!selectedTrimData) return;
        
        // Get vehicle info
        const vehicleResponse = await fetch(`${API_BASE_URL}/vehicles/${state.selectedLevel}`);
        const vehicleData = await vehicleResponse.json();
        
        // Update specs
        document.getElementById('spec-power').textContent = selectedTrimData.power + ' hp';
        document.getElementById('spec-acceleration').textContent = selectedTrimData.acceleration + 's';
        document.getElementById('spec-range').textContent = vehicleData.range + ' mi';
        document.getElementById('spec-top-speed').textContent = '140 mph'; // Simplified
        
        // Update charging times
        const homeCharging = Math.round(vehicleData.battery / 11 * 10) / 10;
        const fastCharging = Math.round((vehicleData.battery * 0.8 / 350) * 60);
        
        document.getElementById('home-charging').textContent = homeCharging + ' hours';
        document.getElementById('fast-charging').textContent = fastCharging + ' min';
        
        // Update environmental impact
        const co2Saved = 23; // 4.6 tons/year * 5 years
        const treesEquivalent = co2Saved * 50;
        
        document.getElementById('co2-saved').textContent = co2Saved + ' tons';
        document.getElementById('trees-equivalent').textContent = formatPrice(treesEquivalent) + ' trees';
        
    } catch (error) {
        console.error('Error updating specs:', error);
    }
}

function updateVehicleImage() {
    if (!state.selectedLevel || !state.selectedColor) {
        return;
    }
    
    const colorLower = state.selectedColor.toLowerCase();
    const imagePath = `images/level${state.selectedLevel}/${colorLower}/${state.currentView}.png`;
    const placeholderUrl = `https://placehold.co/1200x675/667eea/ffffff?text=Level+${state.selectedLevel}+${state.currentView}`;
    
    const vehicleImage = document.getElementById('vehicle-image');
    const imageLoading = document.getElementById('image-loading');
    
    // Show loading
    imageLoading.classList.add('active');
    
    // Try to load actual image, fallback to placeholder
    const img = new Image();
    img.onload = () => {
        vehicleImage.src = imagePath;
        imageLoading.classList.remove('active');
    };
    img.onerror = () => {
        vehicleImage.src = placeholderUrl;
        imageLoading.classList.remove('active');
    };
    img.src = imagePath;
}

function showSummaryCards() {
    document.getElementById('specs-card').style.display = 'block';
    document.getElementById('charging-card').style.display = 'block';
    document.getElementById('environmental-card').style.display = 'block';
    document.getElementById('payment-card').style.display = 'block';
}

// ============================================
// PAYMENT CALCULATOR
// ============================================

function initializePaymentCalculator() {
    const downPaymentInput = document.getElementById('down-payment');
    const loanTermSelect = document.getElementById('loan-term');
    const aprInput = document.getElementById('apr');
    
    downPaymentInput.addEventListener('input', updateMonthlyPayment);
    loanTermSelect.addEventListener('change', updateMonthlyPayment);
    aprInput.addEventListener('input', updateMonthlyPayment);
    
    // Initialize accessories listeners
    const accessoryCheckboxes = document.querySelectorAll('input[name="accessory"]');
    accessoryCheckboxes.forEach(cb => {
        cb.addEventListener('change', () => {
            updateSelectedAccessories();
            updatePricing();
        });
    });
}

function updateMonthlyPayment() {
    const totalPriceText = document.getElementById('total-price').textContent;
    const totalPrice = parseFloat(totalPriceText.replace(/[$,]/g, ''));
    
    if (!totalPrice || totalPrice === 0) {
        document.getElementById('monthly-payment').textContent = '$0';
        return;
    }
    
    const downPayment = parseFloat(document.getElementById('down-payment').value) || 0;
    const loanTerm = parseInt(document.getElementById('loan-term').value) || 60;
    const apr = parseFloat(document.getElementById('apr').value) || 5.9;
    
    const loanAmount = totalPrice - downPayment;
    
    if (loanAmount <= 0) {
        document.getElementById('monthly-payment').textContent = '$0';
        return;
    }
    
    const monthlyRate = (apr / 100) / 12;
    
    let monthlyPayment;
    if (monthlyRate === 0) {
        monthlyPayment = loanAmount / loanTerm;
    } else {
        monthlyPayment = loanAmount * (monthlyRate * Math.pow(1 + monthlyRate, loanTerm)) / (Math.pow(1 + monthlyRate, loanTerm) - 1);
    }
    
    document.getElementById('monthly-payment').textContent = '$' + formatPrice(Math.round(monthlyPayment));
}

// ============================================
// SIGNATURE MODELS
// ============================================

async function initializeSignatureModels() {
    try {
        const response = await fetch(`${API_BASE_URL}/signatures`);
        const data = await response.json();
        
        state.signatureVehicles = data.signatures;
        renderSignatureGrid();
    } catch (error) {
        console.error('Error loading signature models:', error);
    }
}

function renderSignatureGrid() {
    const signatureGrid = document.getElementById('signature-grid');
    signatureGrid.innerHTML = '';
    
    state.signatureVehicles.forEach(signature => {
        const card = document.createElement('div');
        card.className = 'signature-card';
        
        // Determine image based on signature
        const imageUrl = getSignatureImageUrl(signature.id, 'front');
        
        card.innerHTML = `
            <img src="${imageUrl}" alt="${signature.name}" class="signature-card-image">
            <div class="signature-card-content">
                <h3 class="signature-card-title">${signature.name}</h3>
                <p class="signature-card-based">Based on ${signature.basedOn}</p>
                <div class="signature-card-price">$${formatPrice(signature.price)}</div>
                <div class="signature-card-savings">Save $${formatPrice(signature.savings)}</div>
            </div>
        `;
        
        card.addEventListener('click', () => {
            showSignatureDetail(signature.id);
        });
        
        signatureGrid.appendChild(card);
    });
}

function getSignatureImageUrl(signatureId, view) {
    // Map signature to level and color
    const mapping = {
        'urban-commuter': { level: 1, color: 'silver' },
        'trail-titan': { level: 2, color: 'black' },
        'track-beast': { level: 3, color: 'green' },
        'executive': { level: 4, color: 'black' }
    };
    
    const config = mapping[signatureId];
    if (!config) return 'https://placehold.co/1200x675/667eea/ffffff?text=Signature+Vehicle';
    
    return `images/level${config.level}/${config.color}/${view}.png`;
}

async function showSignatureDetail(signatureId) {
    try {
        const response = await fetch(`${API_BASE_URL}/signatures/${signatureId}`);
        const data = await response.json();
        
        state.currentSignature = data;
        
        // Hide grid, show detail
        document.getElementById('signature-grid').style.display = 'none';
        document.querySelector('.signature-intro').style.display = 'none';
        document.getElementById('signature-detail').style.display = 'block';
        
        // Populate detail view
        document.getElementById('sig-detail-title').textContent = data.name;
        document.getElementById('sig-detail-description').textContent = data.description;
        document.getElementById('sig-price').textContent = '$' + formatPrice(data.pricing.signaturePrice);
        document.getElementById('sig-savings').textContent = `Save $${formatPrice(data.pricing.savings)} vs building separately`;
        
        // Update image
        const imageUrl = getSignatureImageUrl(data.id, 'front');
        document.getElementById('signature-image').src = imageUrl;
        
        // Populate specs
        const specsDiv = document.getElementById('sig-specs');
        specsDiv.innerHTML = `
            <div class="spec-item">
                <div class="spec-label">Power</div>
                <div class="spec-value">${data.specifications.power} hp</div>
            </div>
            <div class="spec-item">
                <div class="spec-label">0-60 mph</div>
                <div class="spec-value">${data.specifications.acceleration}s</div>
            </div>
            <div class="spec-item">
                <div class="spec-label">Range</div>
                <div class="spec-value">${data.specifications.range} mi</div>
            </div>
            <div class="spec-item">
                <div class="spec-label">Top Speed</div>
                <div class="spec-value">${data.specifications.topSpeed} mph</div>
            </div>
        `;
        
        // Populate features
        const featuresDiv = document.getElementById('sig-features');
        const featuresList = data.includedFeatures.split('\n').filter(f => f.trim().startsWith('-'));
        featuresDiv.innerHTML = `
            <h4>What's Included</h4>
            <ul>
                ${featuresList.map(f => `<li>${f.replace('-', '').trim()}</li>`).join('')}
            </ul>
        `;
        
    } catch (error) {
        console.error('Error loading signature detail:', error);
    }
}

function updateSignatureImage(view) {
    if (!state.currentSignature) return;
    
    const imageUrl = getSignatureImageUrl(state.currentSignature.id, view);
    const placeholderUrl = `https://placehold.co/1200x675/667eea/ffffff?text=${state.currentSignature.name}+${view}`;
    
    const signatureImage = document.getElementById('signature-image');
    
    // Try to load actual image, fallback to placeholder
    const img = new Image();
    img.onload = () => {
        signatureImage.src = imageUrl;
    };
    img.onerror = () => {
        signatureImage.src = placeholderUrl;
    };
    img.src = imageUrl;
}

// Back button for signature detail
document.getElementById('back-to-grid')?.addEventListener('click', () => {
    document.getElementById('signature-detail').style.display = 'none';
    document.getElementById('signature-grid').style.display = 'grid';
    document.querySelector('.signature-intro').style.display = 'block';
    state.currentSignature = null;
});

// ============================================
// ORDER PLACEMENT
// ============================================

document.getElementById('place-order-btn')?.addEventListener('click', async () => {
    if (!state.selectedLevel || !state.selectedTrim || !state.selectedColor) {
        alert('Please complete your vehicle configuration');
        return;
    }
    
    const orderData = {
        level: state.selectedLevel,
        trim: state.selectedTrim,
        color: state.selectedColor,
        options: state.selectedOptions.map(o => o.name),
        servicePackages: [],
        accessories: state.selectedAccessories.map(a => a.name)
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });
        
        if (!response.ok) {
            throw new Error('Order failed');
        }
        
        const result = await response.json();
        showOrderConfirmation(result);
        
    } catch (error) {
        console.error('Error placing order:', error);
        alert('Failed to place order. Please try again.');
    }
});

document.getElementById('order-signature-btn')?.addEventListener('click', async () => {
    if (!state.currentSignature) {
        alert('Please select a signature vehicle');
        return;
    }
    
    const orderData = {
        signatureName: state.currentSignature.id,
        additionalOptions: [],
        accessories: []
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/order/signature`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });
        
        if (!response.ok) {
            throw new Error('Order failed');
        }
        
        const result = await response.json();
        showOrderConfirmation(result);
        
    } catch (error) {
        console.error('Error placing signature order:', error);
        alert('Failed to place order. Please try again.');
    }
});

function showOrderConfirmation(orderData) {
    const modal = document.getElementById('order-modal');
    const modalBody = document.getElementById('modal-body');
    
    modalBody.innerHTML = `
        <div style="text-align: center; margin-bottom: 1.5rem;">
            <div style="font-size: 3rem; color: var(--success);">✓</div>
            <h3 style="margin-top: 1rem;">Thank You for Your Order!</h3>
        </div>
        
        <div style="background: var(--surface); padding: 1rem; border-radius: 0.5rem; margin-bottom: 1rem;">
            <p><strong>Order ID:</strong> ${orderData.orderId}</p>
            <p><strong>Date:</strong> ${orderData.orderDate}</p>
            ${orderData.isSignature ? `<p><strong>Signature:</strong> ${orderData.signatureName}</p>` : ''}
        </div>
        
        <div style="margin-bottom: 1rem;">
            <h4>Vehicle Details</h4>
            <p><strong>Model:</strong> ${orderData.vehicle.model} ${orderData.vehicle.trim}</p>
            <p><strong>Color:</strong> ${orderData.vehicle.color}</p>
        </div>
        
        <div style="background: var(--surface); padding: 1rem; border-radius: 0.5rem; margin-bottom: 1rem;">
            <h4>Pricing</h4>
            <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem;">
                <span>Total:</span>
                <strong style="color: var(--primary-color); font-size: 1.5rem;">$${formatPrice(orderData.pricing.total)}</strong>
            </div>
            <div style="display: flex; justify-content: space-between; font-size: 0.875rem; color: var(--text-secondary);">
                <span>Estimated Monthly:</span>
                <span>$${formatPrice(orderData.paymentEstimate.monthlyPayment)}/mo</span>
            </div>
        </div>
        
        <p style="font-size: 0.875rem; color: var(--text-secondary); text-align: center;">
            A receipt has been saved to: ${orderData.receiptFile}
        </p>
        
        <p style="font-size: 0.875rem; color: var(--text-secondary); text-align: center; margin-top: 1rem;">
            Our team will contact you shortly to finalize your order.
        </p>
    `;
    
    modal.classList.add('active');
}

// Modal close handlers
document.getElementById('modal-close')?.addEventListener('click', () => {
    document.getElementById('order-modal').classList.remove('active');
});

document.getElementById('order-modal')?.addEventListener('click', (e) => {
    if (e.target.id === 'order-modal') {
        document.getElementById('order-modal').classList.remove('active');
    }
});
