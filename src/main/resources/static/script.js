// ===================================
// global state management
// ===================================

const state = {
    currentPage: 'home',
    currentView: 'modelSelection',
    selectedLevel: null,
    selectedTrim: null,
    selectedColor: null,
    currentImageView: 'front',
    selectedOptions: [],
    selectedAccessories: [],
    basePrice: 0,
    vehicleData: null,
    // signature-specific state
    currentSignature: null,
    signatureData: null,
    signatureAccessories: []
};

// api endpoint
const API_BASE = 'http://localhost:8080/api';

// ===================================
// initialization
// ===================================

document.addEventListener('DOMContentLoaded', () => {
    initializeRouting();
    initializeEventListeners();

    // handle initial route on page load/reload
    // this ensures the correct page shows based on the URL hash
    handleRouteChange();
});

// ===================================
// hash routing for browser navigation
// ===================================

function initializeRouting() {
    // listen for hash changes (back/forward/reload)
    window.addEventListener('hashchange', handleRouteChange);

    // listen for popstate (back/forward buttons)
    window.addEventListener('popstate', handleRouteChange);
}

function handleRouteChange() {
    const hash = window.location.hash || '#/';

    // parse hash to extract page and parameters
    const hashParts = hash.split('?');
    const path = hashParts[0];
    const params = new URLSearchParams(hashParts[1] || '');

    // determine which page to show
    if (path === '#/' || path === '') {
        showPage('home');
    } else if (path === '#build') {
        const level = params.get('level');
        const signature = params.get('signature');

        if (signature && !level) {
            // restore signature view
            restoreSignatureView(signature);
        } else if (level) {
            // restore to configurator view with this level
            restoreBuildConfiguration(parseInt(level), signature);
        } else {
            // just show build page (will show model selection)
            showPage('build');
        }
    } else if (path === '#signatures') {
        showPage('signatures');
    } else {
        // default to home if unknown hash
        window.location.hash = '#/';
        showPage('home');
    }

    // update active nav link
    updateActiveNavLink();
}

// restore build configuration from URL parameters on reload
async function restoreBuildConfiguration(level, signature) {
    if (!level || level < 1 || level > 4) {
        showPage('build');
        return;
    }

    state.selectedLevel = level;
    state.currentImageView = 'front';

    showLoading();

    try {
        // fetch vehicle data from api
        const response = await fetch(`${API_BASE}/vehicles/${level}`);
        if (!response.ok) throw new Error('Failed to fetch vehicle data');

        state.vehicleData = await response.json();

        // show build page
        state.currentPage = 'build';
        document.querySelectorAll('.page').forEach(page => {
            page.classList.remove('active');
        });
        const buildPage = document.getElementById('buildPage');
        if (buildPage) {
            buildPage.classList.add('active');
        }

        // show configurator view
        showView('configuratorView');

        // initialize configurator (with default selections, not saved ones)
        if (signature) {
            initializeSignatureConfigurator(signature);
        } else {
            initializeConfigurator();
        }

        updateActiveNavLink();

    } catch (error) {
        console.error('Error restoring configuration:', error);
        // fallback to build page
        showPage('build');
    } finally {
        hideLoading();
    }
}

// restore signature view from URL parameters on reload
async function restoreSignatureView(signatureName) {
    if (!signatureName) {
        showPage('build');
        return;
    }

    state.currentSignature = signatureName;
    state.currentImageView = 'front';
    state.signatureAccessories = [];

    showLoading();

    try {
        // fetch signature details from api
        const response = await fetch(`${API_BASE}/signatures/${signatureName}`);
        if (!response.ok) throw new Error('Failed to fetch signature data');

        state.signatureData = await response.json();

        // show build page
        state.currentPage = 'build';
        document.querySelectorAll('.page').forEach(page => {
            page.classList.remove('active');
        });
        const buildPage = document.getElementById('buildPage');
        if (buildPage) {
            buildPage.classList.add('active');
        }

        // show signature view
        showView('signatureView');

        // initialize signature details page (with default selections)
        initializeSignatureDetails();

        updateActiveNavLink();

    } catch (error) {
        console.error('Error restoring signature view:', error);
        // fallback to build page
        showPage('build');
    } finally {
        hideLoading();
    }
}

function showPage(pageName) {
    state.currentPage = pageName;

    // update hash if needed (for direct function calls)
    const expectedHash = pageName === 'home' ? '#/' : `#${pageName}`;
    if (window.location.hash !== expectedHash) {
        // use replaceState to avoid adding to history when just syncing
        history.replaceState(null, '', expectedHash);
    }

    // hide all pages
    document.querySelectorAll('.page').forEach(page => {
        page.classList.remove('active');
    });

    // show target page
    const targetPage = document.getElementById(pageName + 'Page');
    if (targetPage) {
        targetPage.classList.add('active');
    }

    // if going to build page, ALWAYS show model selection and reset state
    if (pageName === 'build') {
        showView('modelSelectionView');
        // reset state when going to build page
        state.selectedLevel = null;
        state.selectedTrim = null;
        state.selectedColor = null;
        state.selectedOptions = [];
        state.selectedAccessories = [];
        state.vehicleData = null;
        state.basePrice = 0;
    }

    // update active nav link
    updateActiveNavLink();

    // scroll to top
    window.scrollTo(0, 0);
}

function updateActiveNavLink() {
    const hash = window.location.hash || '#/';
    // extract just the path part (before any ?)
    const path = hash.split('?')[0];

    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');

        if (path === '#/' && link.dataset.page === 'home') {
            link.classList.add('active');
        } else if (path === '#build' && link.dataset.page === 'build') {
            link.classList.add('active');
        } else if (path === '#signatures' && link.dataset.page === 'signatures') {
            link.classList.add('active');
        }
    });
}

// ===================================
// view management (within build page)
// ===================================

function showView(viewId) {
    const buildPage = document.getElementById('buildPage');
    if (!buildPage) return;

    // hide all views
    const views = buildPage.querySelectorAll('.view');
    views.forEach(view => {
        view.classList.remove('active');
    });

    // show target view
    const targetView = document.getElementById(viewId);
    if (targetView) {
        targetView.classList.add('active');
    }

    // update state
    state.currentView = viewId;
}

// ===================================
// event listeners setup
// ===================================

function initializeEventListeners() {
    // model card selection
    const modelCards = document.querySelectorAll('.model-card:not(.signature)');
    modelCards.forEach(card => {
        card.addEventListener('click', handleModelSelection);
    });

    // signature card selection - navigate to signature details page
    const signatureCards = document.querySelectorAll('.model-card.signature');
    signatureCards.forEach(card => {
        card.addEventListener('click', handleSignatureSelection);
    });

    // back to models button (hidden by CSS but keep functionality)
    const backBtn = document.getElementById('backToModels');
    if (backBtn) {
        backBtn.addEventListener('click', () => {
            showView('modelSelectionView');
        });
    }

    // back to signatures button
    const backToSigBtn = document.getElementById('backToSignatures');
    if (backToSigBtn) {
        backToSigBtn.addEventListener('click', () => {
            window.location.hash = '#signatures';
        });
    }

    // view selector buttons (for regular configurator)
    const viewButtons = document.querySelectorAll('.view-btn');
    viewButtons.forEach(btn => {
        btn.addEventListener('click', handleViewSwitch);
    });

    // signature view selector buttons
    const sigViewButtons = document.querySelectorAll('.signature-left .view-btn');
    sigViewButtons.forEach(btn => {
        btn.addEventListener('click', handleSignatureViewSwitch);
    });

    // payment calculator inputs
    const downPayment = document.getElementById('downPayment');
    const interestRate = document.getElementById('interestRate');
    const loanTerm = document.getElementById('loanTerm');

    if (downPayment) downPayment.addEventListener('input', calculateMonthlyPayment);
    if (interestRate) interestRate.addEventListener('input', calculateMonthlyPayment);
    if (loanTerm) loanTerm.addEventListener('change', calculateMonthlyPayment);

    // signature payment calculator inputs
    const sigDownPayment = document.getElementById('sigDownPayment');
    const sigInterestRate = document.getElementById('sigInterestRate');
    const sigLoanTerm = document.getElementById('sigLoanTerm');

    if (sigDownPayment) sigDownPayment.addEventListener('input', calculateSignatureMonthlyPayment);
    if (sigInterestRate) sigInterestRate.addEventListener('input', calculateSignatureMonthlyPayment);
    if (sigLoanTerm) sigLoanTerm.addEventListener('change', calculateSignatureMonthlyPayment);

    // order button
    const orderBtn = document.getElementById('orderBtn');
    if (orderBtn) {
        orderBtn.addEventListener('click', handleOrderSubmission);
    }

    // signature order button
    const signatureOrderBtn = document.getElementById('signatureOrderBtn');
    if (signatureOrderBtn) {
        signatureOrderBtn.addEventListener('click', handleSignatureOrderSubmission);
    }

    // modal buttons
    const closeModalBtn = document.getElementById('closeModalBtn');
    const newOrderBtn = document.getElementById('newOrderBtn');

    if (closeModalBtn) closeModalBtn.addEventListener('click', closeModal);
    if (newOrderBtn) newOrderBtn.addEventListener('click', startNewOrder);
}

// ===================================
// signature selection
// ===================================

async function handleSignatureSelection(e) {
    const card = e.currentTarget;
    const signatureName = card.dataset.signature;

    state.currentSignature = signatureName;
    state.currentImageView = 'front';
    state.signatureAccessories = [];

    showLoading();

    try {
        // fetch signature details from api
        const response = await fetch(`${API_BASE}/signatures/${signatureName}`);
        if (!response.ok) throw new Error('Failed to fetch signature data');

        state.signatureData = await response.json();

        // navigate to build page with signature parameter
        window.location.hash = `#build?signature=${signatureName}`;

        // show build page
        state.currentPage = 'build';
        document.querySelectorAll('.page').forEach(page => {
            page.classList.remove('active');
        });
        const buildPage = document.getElementById('buildPage');
        if (buildPage) {
            buildPage.classList.add('active');
        }

        // show signature view
        showView('signatureView');

        // initialize signature details page
        initializeSignatureDetails();

        updateActiveNavLink();

    } catch (error) {
        console.error('Error loading signature:', error);
        alert('Failed to load signature vehicle. Please try again.');
    } finally {
        hideLoading();
    }
}

// initialize configurator with signature pre-selected options
function initializeSignatureConfigurator(signatureName) {
    if (!state.vehicleData) return;

    // scroll right panel to top
    const rightPanel = document.querySelector('.config-right');
    if (rightPanel) {
        rightPanel.scrollTop = 0;
    }

    // scroll left panel to top
    const leftPanel = document.querySelector('.config-left');
    if (leftPanel) {
        leftPanel.scrollTop = 0;
    }

    // scroll main window to top
    window.scrollTo(0, 0);

    // signature configurations
    const signatureConfigs = {
        'urban-commuter': {
            trim: 'Premium',
            color: 'silver',
            options: ['enhanced-autopilot']
        },
        'trail-titan': {
            trim: 'Off-Road',
            color: 'black',
            options: []
        },
        'track-beast': {
            trim: 'Ultra',
            color: 'green',
            options: ['track-package']
        },
        'executive': {
            trim: 'Flagship',
            color: 'black',
            options: ['massage-seats']
        }
    };

    const config = signatureConfigs[signatureName];
    if (!config) {
        // fallback to regular initialization
        initializeConfigurator();
        return;
    }

    // find the matching trim
    const trims = state.vehicleData.trims || [];
    const matchingTrim = trims.find(t => t.name === config.trim);

    if (matchingTrim) {
        state.selectedTrim = matchingTrim.name;
        state.basePrice = matchingTrim.price;
    } else if (trims.length > 0) {
        state.selectedTrim = trims[0].name;
        state.basePrice = trims[0].price;
    }

    // set color
    const colors = state.vehicleData.colors || [];
    const matchingColor = colors.find(c => c.name.toLowerCase() === config.color);

    if (matchingColor) {
        state.selectedColor = matchingColor.name.toLowerCase();
    } else if (colors.length > 0) {
        state.selectedColor = colors[0].name.toLowerCase();
    }

    // pre-select options
    state.selectedOptions = config.options || [];
    state.selectedAccessories = [];

    // render configurator sections
    renderTrimOptions();
    renderColorOptions();
    renderTrimFeatures(); // render features for signature trim
    renderOptions();
    renderAccessories();
    updateVehicleImage();
    updatePricing();
    updateSpecifications();
}

// ===================================
// signature details page initialization
// ===================================

function initializeSignatureDetails() {
    if (!state.signatureData) return;

    // scroll both panels to top
    const rightPanel = document.querySelector('.signature-right');
    if (rightPanel) {
        rightPanel.scrollTop = 0;
    }

    const leftPanel = document.querySelector('.signature-left');
    if (leftPanel) {
        leftPanel.scrollTop = 0;
    }

    window.scrollTo(0, 0);

    const data = state.signatureData;

    // populate header
    updateElement('signatureName', data.name);
    updateElement('signatureBasedOn', 'Based on ' + data.basedOn);

    // populate pricing
    updateElement('signaturePrice', '$' + data.pricing.signaturePrice.toLocaleString());
    updateElement('signatureRegularPrice', '$' + data.pricing.regularPrice.toLocaleString());
    updateElement('signatureSavings', '$' + data.pricing.savings.toLocaleString());

    // populate description and target
    updateElement('signatureDescription', data.description);
    updateElement('signatureTarget', data.targetCustomer);

    // populate what's included
    renderSignatureFeatures(data.includedFeatures);

    // populate specifications
    const specs = data.specifications;
    updateElement('sigSpecPower', specs.power + ' hp');
    updateElement('sigSpecAccel', specs.acceleration + 's');
    updateElement('sigSpecRange', specs.range + ' mi');
    updateElement('sigSpecBattery', specs.battery + ' kWh');

    // render accessories (only accessories, no options)
    renderSignatureAccessories();

    // update vehicle image
    updateSignatureImage();

    // calculate initial pricing
    updateSignaturePricing();
}

// render signature included features list
function renderSignatureFeatures(featuresText) {
    const container = document.getElementById('signatureIncluded');
    if (!container) return;

    container.innerHTML = '';

    // split features by newline and filter out empty lines
    const features = featuresText.split('\n').filter(f => f.trim());

    features.forEach(feature => {
        const featureDiv = document.createElement('div');
        featureDiv.className = 'signature-feature-item';
        featureDiv.textContent = feature.replace(/^[-•*]\s*/, '').trim();
        container.appendChild(featureDiv);
    });
}

// render accessories for signature page
function renderSignatureAccessories() {
    const container = document.getElementById('signatureAccessoriesList');
    if (!container) return;

    container.innerHTML = '';

    // use the same accessories as regular configurator
    const accessories = [
        { id: 'floor-mats', name: 'Premium Floor Mats', price: 400 },
        { id: 'home-charger', name: 'Home EV Charger (Level 2, 240V)', price: 800 },
        { id: 'paint-protection', name: 'Paint Protection Film (Full Front)', price: 2000 },
        { id: 'ceramic-coating', name: 'Ceramic Coating (Full Vehicle)', price: 1500 }
    ];

    accessories.forEach(accessory => {
        const accessoryDiv = document.createElement('div');
        accessoryDiv.className = 'accessory-item';
        accessoryDiv.innerHTML = `
            <div class="accessory-checkbox"></div>
            <div class="accessory-info">
                <div class="accessory-name">${accessory.name}</div>
                <div class="accessory-price">+$${accessory.price.toLocaleString()}</div>
            </div>
        `;

        accessoryDiv.addEventListener('click', () => {
            const isSelected = accessoryDiv.classList.contains('selected');

            if (isSelected) {
                accessoryDiv.classList.remove('selected');
                state.signatureAccessories = state.signatureAccessories.filter(id => id !== accessory.id);
            } else {
                accessoryDiv.classList.add('selected');
                state.signatureAccessories.push(accessory.id);
            }

            updateSignaturePricing();
        });

        container.appendChild(accessoryDiv);
    });
}

// update signature vehicle image
function updateSignatureImage() {
    const img = document.getElementById('signatureVehicleImage');
    if (!img || !state.signatureData) return;

    const specs = state.signatureData.specifications;
    const color = specs.color.toLowerCase();
    const view = state.currentImageView;

    // map signature to level for image path
    const signatureToLevel = {
        'urban-commuter': 1,
        'trail-titan': 2,
        'track-beast': 3,
        'executive': 4
    };

    const level = signatureToLevel[state.currentSignature];
    const imagePath = `images/level${level}/${color}/${view}.png`;
    img.src = imagePath;
    img.alt = `${state.signatureData.name} ${capitalizeFirst(view)} view`;
}

// update signature pricing calculations
function updateSignaturePricing() {
    if (!state.signatureData) return;

    const basePrice = state.signatureData.pricing.signaturePrice;

    // calculate accessories total
    let accessoriesTotal = 0;
    const accessories = [
        { id: 'floor-mats', price: 400 },
        { id: 'home-charger', price: 800 },
        { id: 'paint-protection', price: 2000 },
        { id: 'ceramic-coating', price: 1500 }
    ];

    accessories.forEach(accessory => {
        if (state.signatureAccessories.includes(accessory.id)) {
            accessoriesTotal += accessory.price;
        }
    });

    // calculate totals
    const subtotal = basePrice + accessoriesTotal;
    const taxRate = 0.085;
    const tax = subtotal * taxRate;
    const total = subtotal + tax;

    // update display
    updateElement('sigBasePrice', '$' + basePrice.toLocaleString());
    updateElement('sigAccessoriesPrice', '$' + accessoriesTotal.toLocaleString());
    updateElement('sigTaxAmount', '$' + Math.round(tax).toLocaleString());
    updateElement('sigTotalPrice', '$' + Math.round(total).toLocaleString());

    // show/hide accessories row
    toggleElement('sigAccessoriesPriceRow', accessoriesTotal > 0);

    // update payment calculator
    calculateSignatureMonthlyPayment();
}

// calculate signature monthly payment
function calculateSignatureMonthlyPayment() {
    const totalPriceText = document.getElementById('sigTotalPrice')?.textContent || '$0';
    const totalPrice = parseInt(totalPriceText.replace(/[$,]/g, ''));

    const downPayment = parseFloat(document.getElementById('sigDownPayment')?.value || 0);
    const interestRate = parseFloat(document.getElementById('sigInterestRate')?.value || 5.9) / 100 / 12;
    const loanTerm = parseInt(document.getElementById('sigLoanTerm')?.value || 60);

    const principal = totalPrice - downPayment;

    if (principal <= 0 || loanTerm === 0) {
        updateElement('sigMonthlyPayment', '$0');
        return;
    }

    // monthly payment formula
    const monthlyPayment = principal * (interestRate * Math.pow(1 + interestRate, loanTerm)) /
                          (Math.pow(1 + interestRate, loanTerm) - 1);

    updateElement('sigMonthlyPayment', '$' + Math.round(monthlyPayment).toLocaleString());
}

// ===================================
// model selection
// ===================================

async function handleModelSelection(e) {
    const card = e.currentTarget;
    const level = card.dataset.level;

    state.selectedLevel = level;
    state.currentImageView = 'front';

    showLoading();

    try {
        // fetch vehicle data from api
        const response = await fetch(`${API_BASE}/vehicles/${level}`);
        if (!response.ok) throw new Error('Failed to fetch vehicle data');

        state.vehicleData = await response.json();

        // navigate to build page with level parameter
        window.location.hash = `#build?level=${level}`;

        // show configurator view
        showView('configuratorView');

        // initialize configurator
        initializeConfigurator();

    } catch (error) {
        console.error('Error loading vehicle:', error);
        alert('Failed to load vehicle data. Please try again.');
    } finally {
        hideLoading();
    }
}

// ===================================
// configurator initialization
// ===================================

function initializeConfigurator() {
    if (!state.vehicleData) return;

    // scroll right panel to top
    const rightPanel = document.querySelector('.config-right');
    if (rightPanel) {
        rightPanel.scrollTop = 0;
    }

    // scroll left panel to top (in case it has overflow)
    const leftPanel = document.querySelector('.config-left');
    if (leftPanel) {
        leftPanel.scrollTop = 0;
    }

    // scroll main window to top
    window.scrollTo(0, 0);

    // set initial trim and color
    const trims = state.vehicleData.trims || [];
    if (trims.length > 0) {
        state.selectedTrim = trims[0].name;
        state.basePrice = trims[0].price;
    }

    const colors = state.vehicleData.colors || [];
    if (colors.length > 0) {
        state.selectedColor = colors[0].name.toLowerCase();
    }

    // reset selections
    state.selectedOptions = [];
    state.selectedAccessories = [];

    // render configurator sections
    renderTrimOptions();
    renderColorOptions();
    renderTrimFeatures(); // render features for default trim
    renderOptions();
    renderAccessories();
    updateVehicleImage();
    updatePricing();
    updateSpecifications();
}

// ===================================
// trim options rendering
// ===================================

function renderTrimOptions() {
    const container = document.getElementById('trimOptions');
    if (!container) return;

    container.innerHTML = '';

    const trims = state.vehicleData.trims || [];
    trims.forEach((trim, index) => {
        const trimDiv = document.createElement('div');
        trimDiv.className = `trim-option ${index === 0 ? 'selected' : ''}`;
        trimDiv.innerHTML = `
            <div class="trim-name">${trim.name}</div>
            <div class="trim-price">$${trim.price.toLocaleString()}</div>
        `;

        trimDiv.addEventListener('click', () => {
            // update selection
            document.querySelectorAll('.trim-option').forEach(t => {
                t.classList.remove('selected');
            });
            trimDiv.classList.add('selected');

            state.selectedTrim = trim.name;
            state.basePrice = trim.price;

            updatePricing();
            updateSpecifications();
            renderTrimFeatures(); // update features when trim changes
        });

        container.appendChild(trimDiv);
    });
}

// ===================================
// render what's included for selected trim
// ===================================

function renderTrimFeatures() {
    const container = document.getElementById('trimFeaturesList');
    if (!container || !state.selectedLevel || !state.selectedTrim) return;

    container.innerHTML = '';

    // get features based on level and trim
    const features = getTrimFeatures(state.selectedLevel, state.selectedTrim);

    if (!features || features.length === 0) {
        container.innerHTML = '<p style="color: #999; font-size: 0.875rem;">No additional features information available.</p>';
        return;
    }

    features.forEach(feature => {
        const featureDiv = document.createElement('div');
        featureDiv.className = 'feature-item';
        featureDiv.textContent = feature;
        container.appendChild(featureDiv);
    });
}

// get features array based on level and trim
function getTrimFeatures(level, trim) {
    const features = {
        // Level 1 features
        1: {
            'Standard': [
                'Basic leather seats with power adjustment',
                '15-inch touchscreen with navigation',
                'Basic Autopilot (lane keeping, adaptive cruise)',
                'Glass panoramic roof',
                'Heated and ventilated front seats',
                '360-degree cameras and parking sensors',
                'Wireless phone charging'
            ],
            'Premium': [
                'All Standard features included',
                'Premium audio system (18 speakers)',
                'Upgraded Nappa leather seats',
                '20-inch alloy wheels',
                'Enhanced ambient lighting',
                'Ventilated rear seats',
                'Premium interior materials'
            ],
            'Performance': [
                'All Standard features included',
                'Sport seats with aggressive bolsters',
                'Adaptive sport suspension',
                'Performance brakes (larger, upgraded)',
                '20-inch performance wheels',
                'Track Mode and Launch Control',
                'Carbon fiber accents'
            ]
        },
        // Level 2 features
        2: {
            'Standard': [
                'Same leather quality as Level 1',
                '15-inch touchscreen with navigation',
                'Basic Autopilot',
                'Glass panoramic roof',
                'Heated/ventilated front seats',
                'Heated rear seats (all 3 rows)',
                '360-degree cameras',
                'Power folding third row seats'
            ],
            'Premium': [
                'All Standard features included',
                'Premium audio system (22 speakers)',
                'Upgraded Nappa leather throughout',
                '22-inch alloy wheels',
                'Executive second-row captain\'s chairs',
                'Ventilated second-row seats',
                'Premium wood trim accents'
            ],
            'Off-Road': [
                'All Standard features included',
                'Lifted air suspension (+2 inches clearance)',
                'Off-road drive modes (Rock, Sand, Mud)',
                'Underbody protection (skid plates)',
                'All-terrain tires on 20-inch wheels',
                'Front tow hooks',
                'Hill descent control'
            ]
        },
        // Level 3 features
        3: {
            'Pro': [
                'Sport seats with aggressive bolsters',
                'Carbon fiber interior trim',
                'Alcantara steering wheel',
                '15-inch touchscreen',
                'Track Mode and Launch Control',
                'Carbon ceramic brakes',
                'Adaptive sport suspension',
                '21-inch forged wheels'
            ],
            'Max': [
                'All Pro features included',
                'Premium Nappa leather sport seats',
                'Premium audio system (24 speakers)',
                'Full Alcantara headliner',
                'Extended carbon fiber package',
                '21-inch lightweight forged wheels',
                'Enhanced ambient lighting'
            ],
            'Ultra': [
                'All Max features included',
                'Full carbon fiber exterior package',
                'Track suspension (race-tuned)',
                'Ultra-lightweight carbon wheels',
                'Track telemetry system with GPS',
                'Performance data recorder',
                'Active rear wing'
            ]
        },
        // Level 4 features
        4: {
            'Flagship': [
                'Premium Nappa leather everywhere',
                'Real wood trim (Sapele)',
                '24+ speaker ultra-premium audio',
                'Adaptive air suspension (self-leveling)',
                'Full Self-Driving Capability (included)',
                'Executive rear seats (zero-gravity mode)',
                'Refrigerator/cooler between rear seats',
                'Starlight headliner (LED constellation)',
                '360-degree cameras with night vision',
                'AR head-up display',
                'Rear-wheel steering'
            ]
        }
    };

    return features[level]?.[trim] || [];
}

// ===================================
// color options rendering
// ===================================

function renderColorOptions() {
    const container = document.getElementById('colorOptions');
    if (!container) return;

    container.innerHTML = '';

    const colors = state.vehicleData.colors || [];
    colors.forEach((color, index) => {
        const colorDiv = document.createElement('div');
        colorDiv.className = `color-option ${index === 0 ? 'selected' : ''}`;

        const colorSwatch = document.createElement('div');
        colorSwatch.className = 'color-swatch';
        colorSwatch.style.backgroundColor = color.hex;

        const colorName = document.createElement('div');
        colorName.className = 'color-name';
        // capitalize first letter of color name
        colorName.textContent = capitalizeFirst(color.name);

        colorDiv.appendChild(colorSwatch);
        colorDiv.appendChild(colorName);

        colorDiv.addEventListener('click', () => {
            // update selection
            document.querySelectorAll('.color-option').forEach(c => {
                c.classList.remove('selected');
            });
            colorDiv.classList.add('selected');

            state.selectedColor = color.name.toLowerCase();
            updateVehicleImage();
        });

        container.appendChild(colorDiv);
    });
}

// ===================================
// options rendering
// ===================================

function renderOptions() {
    const container = document.getElementById('optionsList');
    if (!container) return;

    container.innerHTML = '';

    const options = state.vehicleData.options || [];
    options.forEach(option => {
        const optionDiv = document.createElement('div');
        optionDiv.className = 'option-item';
        optionDiv.innerHTML = `
            <div class="option-checkbox"></div>
            <div class="option-info">
                <div class="option-name">${option.name}</div>
                <div class="option-price">+$${option.price.toLocaleString()}</div>
            </div>
        `;

        optionDiv.addEventListener('click', () => {
            const isSelected = optionDiv.classList.contains('selected');

            if (isSelected) {
                optionDiv.classList.remove('selected');
                state.selectedOptions = state.selectedOptions.filter(id => id !== option.id);
            } else {
                optionDiv.classList.add('selected');
                state.selectedOptions.push(option.id);
            }

            updatePricing();
        });

        container.appendChild(optionDiv);
    });
}

// ===================================
// accessories rendering
// ===================================

function renderAccessories() {
    const container = document.getElementById('accessoriesList');
    if (!container) return;

    container.innerHTML = '';

    const accessories = state.vehicleData.accessories || [];
    accessories.forEach(accessory => {
        const accessoryDiv = document.createElement('div');
        accessoryDiv.className = 'accessory-item';
        accessoryDiv.innerHTML = `
            <div class="accessory-checkbox"></div>
            <div class="accessory-info">
                <div class="accessory-name">${accessory.name}</div>
                <div class="accessory-price">+$${accessory.price.toLocaleString()}</div>
            </div>
        `;

        accessoryDiv.addEventListener('click', () => {
            const isSelected = accessoryDiv.classList.contains('selected');

            if (isSelected) {
                accessoryDiv.classList.remove('selected');
                state.selectedAccessories = state.selectedAccessories.filter(id => id !== accessory.id);
            } else {
                accessoryDiv.classList.add('selected');
                state.selectedAccessories.push(accessory.id);
            }

            updatePricing();
        });

        container.appendChild(accessoryDiv);
    });
}

// ===================================
// vehicle image management
// ===================================

function updateVehicleImage() {
    const img = document.getElementById('mainVehicleImage');
    if (!img) return;

    const level = state.selectedLevel;
    const color = state.selectedColor;
    const view = state.currentImageView;

    const imagePath = `images/level${level}/${color}/${view}.png`;
    img.src = imagePath;
    img.alt = `Level ${level} ${capitalizeFirst(color)} ${capitalizeFirst(view)} view`;
}

function handleViewSwitch(e) {
    const view = e.currentTarget.dataset.view;

    // update button states
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    e.currentTarget.classList.add('active');

    // update state and image
    state.currentImageView = view;
    updateVehicleImage();
}

// handle signature image view switching
function handleSignatureViewSwitch(e) {
    const view = e.currentTarget.dataset.view;

    // update button states in signature view
    const sigViewButtons = document.querySelectorAll('.signature-left .view-btn');
    sigViewButtons.forEach(btn => {
        btn.classList.remove('active');
    });
    e.currentTarget.classList.add('active');

    // update state and image
    state.currentImageView = view;
    updateSignatureImage();
}

// ===================================
// pricing calculations
// ===================================

function updatePricing() {
    let optionsTotal = 0;
    let accessoriesTotal = 0;

    // calculate options total
    if (state.vehicleData && state.vehicleData.options) {
        state.vehicleData.options.forEach(option => {
            if (state.selectedOptions.includes(option.id)) {
                optionsTotal += option.price;
            }
        });
    }

    // calculate accessories total
    if (state.vehicleData && state.vehicleData.accessories) {
        state.vehicleData.accessories.forEach(accessory => {
            if (state.selectedAccessories.includes(accessory.id)) {
                accessoriesTotal += accessory.price;
            }
        });
    }

    // calculate totals
    const subtotal = state.basePrice + optionsTotal + accessoriesTotal;
    const taxRate = 0.085;
    const tax = subtotal * taxRate;
    const total = subtotal + tax;

    // update display
    updateElement('basePrice', `$${state.basePrice.toLocaleString()}`);
    updateElement('optionsPrice', `$${optionsTotal.toLocaleString()}`);
    updateElement('accessoriesPrice', `$${accessoriesTotal.toLocaleString()}`);
    updateElement('taxAmount', `$${Math.round(tax).toLocaleString()}`);
    updateElement('totalPrice', `$${Math.round(total).toLocaleString()}`);

    // show/hide price rows
    toggleElement('optionsPriceRow', optionsTotal > 0);
    toggleElement('accessoriesPriceRow', accessoriesTotal > 0);

    // update payment calculator
    calculateMonthlyPayment();
}

// ===================================
// payment calculator
// ===================================

function calculateMonthlyPayment() {
    const totalPriceText = document.getElementById('totalPrice')?.textContent || '$0';
    const totalPrice = parseInt(totalPriceText.replace(/[$,]/g, ''));

    const downPayment = parseFloat(document.getElementById('downPayment')?.value || 0);
    const interestRate = parseFloat(document.getElementById('interestRate')?.value || 5.9) / 100 / 12;
    const loanTerm = parseInt(document.getElementById('loanTerm')?.value || 60);

    const principal = totalPrice - downPayment;

    if (principal <= 0 || loanTerm === 0) {
        updateElement('monthlyPayment', '$0');
        return;
    }

    // monthly payment formula: P * [r(1+r)^n] / [(1+r)^n - 1]
    const monthlyPayment = principal * (interestRate * Math.pow(1 + interestRate, loanTerm)) /
                          (Math.pow(1 + interestRate, loanTerm) - 1);

    updateElement('monthlyPayment', `$${Math.round(monthlyPayment).toLocaleString()}`);
}

// ===================================
// specifications update
// ===================================

function updateSpecifications() {
    if (!state.vehicleData) return;

    const currentTrim = state.vehicleData.trims?.find(t => t.name === state.selectedTrim);
    if (!currentTrim) return;

    updateElement('specPower', `${currentTrim.power} hp`);
    updateElement('specAccel', `${currentTrim.acceleration}s`);
    updateElement('specRange', `${currentTrim.range} mi`);
    updateElement('specBattery', `${currentTrim.battery} kWh`);
}

// ===================================
// order submission
// ===================================

async function handleOrderSubmission() {
    if (!state.selectedLevel || !state.selectedTrim || !state.selectedColor) {
        alert('Please complete your vehicle configuration.');
        return;
    }

    showLoading();

    try {
        // prepare order data
        const orderData = {
            level: parseInt(state.selectedLevel),
            trim: state.selectedTrim,
            color: state.selectedColor,
            options: state.selectedOptions,
            servicePackages: [],
            accessories: state.selectedAccessories
        };

        // submit order
        const response = await fetch(`${API_BASE}/order`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) throw new Error('Order submission failed');

        const result = await response.json();

        // show success modal
        showOrderConfirmation(result);

    } catch (error) {
        console.error('Error submitting order:', error);
        alert('Failed to submit order. Please try again.');
    } finally {
        hideLoading();
    }
}

// handle signature vehicle order submission
async function handleSignatureOrderSubmission() {
    if (!state.currentSignature) {
        alert('No signature selected.');
        return;
    }

    showLoading();

    try {
        // prepare signature order data
        const orderData = {
            signatureName: state.currentSignature,
            additionalOptions: [], // signatures don't allow additional options
            accessories: state.signatureAccessories
        };

        // submit signature order
        const response = await fetch(`${API_BASE}/order/signature`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });

        if (!response.ok) throw new Error('Signature order submission failed');

        const result = await response.json();

        // show success modal
        showOrderConfirmation(result);

    } catch (error) {
        console.error('Error submitting signature order:', error);
        alert('Failed to submit signature order. Please try again.');
    } finally {
        hideLoading();
    }
}

// ===================================
// order confirmation modal
// ===================================

function showOrderConfirmation(orderData) {
    const modal = document.getElementById('orderModal');
    const orderIdSpan = document.getElementById('orderId');
    const summaryDiv = document.getElementById('orderSummary');

    if (!modal) return;

    // set order id
    if (orderIdSpan) {
        orderIdSpan.textContent = orderData.orderId || 'N/A';
    }

    // set order summary
    if (summaryDiv) {
        summaryDiv.innerHTML = `
            <p><strong>Vehicle:</strong> Level ${state.selectedLevel} ${state.selectedTrim}</p>
            <p><strong>Color:</strong> ${capitalizeFirst(state.selectedColor)}</p>
            <p><strong>Total:</strong> ${document.getElementById('totalPrice')?.textContent || '$0'}</p>
        `;
    }

    // show modal
    modal.classList.add('active');
}

function closeModal() {
    const modal = document.getElementById('orderModal');
    if (modal) {
        modal.classList.remove('active');
    }
}

function startNewOrder() {
    closeModal();

    // reset state
    state.selectedLevel = null;
    state.selectedTrim = null;
    state.selectedColor = null;
    state.currentImageView = 'front';
    state.selectedOptions = [];
    state.selectedAccessories = [];
    state.basePrice = 0;
    state.vehicleData = null;

    // go back to model selection
    showView('modelSelectionView');
}

// ===================================
// loading overlay
// ===================================

function showLoading() {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.classList.remove('hidden');
    }
}

function hideLoading() {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.classList.add('hidden');
    }
}

// ===================================
// utility functions
// ===================================

function updateElement(id, content) {
    const element = document.getElementById(id);
    if (element) {
        element.textContent = content;
    }
}

function toggleElement(id, show) {
    const element = document.getElementById(id);
    if (element) {
        if (show) {
            element.classList.remove('hidden');
        } else {
            element.classList.add('hidden');
        }
    }
}

function capitalizeFirst(str) {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1);
}